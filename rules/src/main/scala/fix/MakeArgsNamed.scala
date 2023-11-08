package fix

import scalafix.v1._

import scala.meta._

class MakeArgsNamed extends SemanticRule("MakeArgsNamed") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case Term.Apply.After_4_6_0(fun, args) => {
        val parameters = getParameters(fun.symbol)
        args.zip(parameters).map {
          case (arg, name) if !arg.symbol.info.exists(_.isParameter) =>
            Patch.addLeft(arg, s"$name = ")
          case _ => Patch.empty
        }
      }
    }
  }.flatten.asPatch

  private def getParameters(symbol: Symbol)(implicit doc: SemanticDocument): List[String] = {
    symbol.info.map(_.signature) match {
      case Some(MethodSignature(_, parameters, _)) =>
        parameters.flatten.map(_.displayName)
      case _ => Nil
    }
  }
}

