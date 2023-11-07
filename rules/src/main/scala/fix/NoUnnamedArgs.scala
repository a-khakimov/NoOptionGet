package fix

import scalafix.v1._

import scala.meta._

class NoUnnamedArgs extends SemanticRule("NoUnnamedArgs") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case method @ Term.Apply.After_4_6_0(m : Term.Name, args: Term.ArgClause) => {
        val parameters = getParameters(m.symbol)
        println(s"-------> ${method.symbol} ${parameters}")
        args.zip(parameters).map {
          case (arg, name) if !arg.symbol.info.exists(_.isParameter) => // WTF?
            println(s">> $arg: $name ------ ${arg.symbol.info.map(_.isParameter)}")
            Patch.addLeft(arg, s"$name = ")
          case _ => Patch.empty
        }
      }
      case method@Term.Apply.After_4_6_0(m @ Term.Name("Foo"), args: Term.ArgClause) => {
        println(s"??? -------> ${method.symbol}")
        List(Patch.empty)
      }
    }
  }.flatten.asPatch

  private def getParameters(symbol: Symbol)(implicit doc: SemanticDocument): List[String] = {
    symbol.info.map(_.signature) match {
      case _ @ Some(MethodSignature(_, parameterLists, _)) =>
        parameterLists.flatten.map(_.displayName)
      case _ => Nil
    }
  }
}

