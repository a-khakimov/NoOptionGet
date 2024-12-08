package fix

import scalafix.v1._

import scala.collection.mutable
import scala.meta._

class NoRecursion extends SemanticRule("NoRecursion") {

  override def description: String = "Find recursion"

  trait Pos {
    def pos: Position
  }

  case class Meth(name: String)

  object Meth {
    def make(name: String, position: Position): Meth with Pos = new Meth(name) with Pos {
      override def pos: Position = position
      override def toString: String = name
    }
  }

  override def fix(implicit doc: SemanticDocument): Patch = {

    // fixme: Use immutable Map
    val callGraph = mutable.Map.empty[Meth with Pos, mutable.Set[Meth with Pos]]

    doc.tree.collect {
      case _ @ Defn.Object(_, name, templ) =>
        collectMethods(name.value, templ, callGraph)
      case _ @ Defn.Class.After_4_6_0(_, name, _, _, templ) =>
        collectMethods(name.value, templ, callGraph)
    }

    val cycles = findCycle(callGraph.toMap.map { case (k, v) => k -> v.toSet })

    cycles.map { cycle =>
      Patch.lint(
        Diagnostic(
          "DetectedRecursion", s"Recursion detected: ${cycle.map(_.name).mkString(" -> ")}",
          cycle.headOption.map(_.pos).getOrElse(doc.tree.pos)
        )
      )
    }.asPatch
  }

  private def collectMethods(
    qualifier: String,
    templ: Template,
    callGraph: mutable.Map[Meth with Pos, mutable.Set[Meth with Pos]]
  )(implicit doc: SemanticDocument): Unit = {
    templ.stats.foreach {
      case defn @ Defn.Def.After_4_6_0(_, name, _, _, body) =>
        val params = getParameterTypes(defn.symbol)
        val fullName = s"$qualifier.${name.value}(${params.mkString(",")})"
        val calls = collectFunctionCalls(body).map(
          c => if (c.name.contains(".")) c else Meth.make(s"$qualifier.$c", defn.pos)  // fixme: This is 🩼
        )
        callGraph.getOrElseUpdate(Meth.make(fullName, defn.pos), mutable.Set.empty) ++= calls
        collectMethodsWithNestedFunctions(fullName, body, callGraph)
      case _ =>
    }
  }

  private def getParameterTypes(symbol: Symbol)(implicit doc: SemanticDocument): List[String] = {
    symbol.info.map(_.signature) match {
      case Some(MethodSignature(_, parameters, _)) => parameters.flatten.map(_.signature.toString())
      case _ => Nil
    }
  }

  private def collectFunctionCalls(body: Term)(implicit doc: SemanticDocument): Set[Meth with Pos] = {
    body.collect {
      case defn @ Term.Apply.After_4_6_0(Term.Select(Term.This(_), Term.Name(methodName)), _) =>
        val className = defn.symbol.owner.info.map(_.displayName + ".").getOrElse("")
        val params = getParameterTypes(defn.symbol)
        val method = Meth.make(s"${className}$methodName(${params.mkString(",")})", defn.pos)
        Some(method)
      case defn @ Term.Apply.After_4_6_0(Term.Name(methodName), _) =>
        val className = defn.symbol.owner.info.map(_.displayName + ".").getOrElse("")
        val params = getParameterTypes(defn.symbol)
        val method = Meth.make(s"${className}$methodName(${params.mkString(",")})", defn.pos)
        Some(method)
      case defn @ Term.Apply.After_4_6_0(Term.Select(receiver, Term.Name(methodName)), _) if !receiver.is[Term.This] && !receiver.is[Term.Name] =>
        val className = defn.symbol.owner.info.map(_.displayName + ".").getOrElse("")
        val params = getParameterTypes(defn.symbol)
        val method = Meth.make(s"${className}$methodName(${params.mkString(",")})", defn.pos)
        Some(method)
      // Ignore super.methodName
      case _ @ Term.Apply.After_4_6_0(Term.Select(Term.Super(_, _), Term.Name(_)), _) =>
        None
    }.toSet
  }.flatten

  private def collectMethodsWithNestedFunctions(
    qualifier: String,
    body: Term,
    callGraph: mutable.Map[Meth with Pos, mutable.Set[Meth with Pos]]
  )(implicit doc: SemanticDocument): Unit = {
    body.collect {
      case defn @ Defn.Def.After_4_6_0(_, name, _, _, innerBody) =>
        val params = getParameterTypes(defn.symbol)
        val fullName = s"$qualifier.${name.value}(${params.mkString(",")})"
        val calls = collectFunctionCalls(innerBody).map {
          case call if call.name.contains(".") => call
          case call                       => Meth.make(s"$qualifier.$call", defn.pos) // fixme: This is 🩼
        }
        callGraph.getOrElseUpdate(Meth.make(fullName, defn.pos), mutable.Set.empty) ++= calls
        collectMethodsWithNestedFunctions(fullName, innerBody, callGraph)
    }
  }

  def findCycle(graph: Map[Meth with Pos, Set[Meth with Pos]]): Option[List[Meth with Pos]] = {
    def dfs(
      node: Meth with Pos,
      visited: Set[Meth with Pos],
      inStack: Set[Meth with Pos],
      path: List[Meth with Pos]
    ): Option[List[Meth with Pos]] = {
      if (inStack.contains(node)) Some((node :: path.takeWhile(_ != node)).reverse)
      else if (visited.contains(node)) None
      else graph.getOrElse(node, Set.empty).toList.foldLeft(Option.empty[List[Meth with Pos]]) {
        case (cycle @ Some(_), _) => cycle
        case (None, neighbor) => dfs(neighbor, visited + node, inStack + node, node :: path)
      }
    }

    graph.keys.flatMap(node => dfs(node, Set.empty, Set.empty, Nil)).headOption
  }
}
