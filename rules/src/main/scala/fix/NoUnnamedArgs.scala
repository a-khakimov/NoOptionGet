package fix

import metaconfig.{ConfDecoder, Configured}
import metaconfig.generic.Surface
import scalafix.v1._

import scala.meta._

case class NoUnnamedArgs(config: NoUnnamedArgsConfig) extends SemanticRule("NoUnnamedArgs") {

  def this() = this(NoUnnamedArgsConfig.default)

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case Term.Apply.After_4_6_0(fun, args) =>
        val parameters = getParameters(fun.symbol)
        if (parameters.size > config.minUnnamedArgs) {
          args.values.zip(parameters).map {
            case (arg, name) if !arg.symbol.info.exists(_.isParameter) =>
              Patch.lint(Diagnostic("", s"Unnamed arguments is not allowed - $name", arg.pos))
            case _ => Patch.empty
          }
        } else Nil
    }
  }.flatten.asPatch

  import NoUnnamedArgsConfig.decoder

  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf.getOrElse("NoUnnamedArgs")(
      NoUnnamedArgsConfig.default
    ).map(NoUnnamedArgs)
  }

  private def getParameters(symbol: Symbol)(implicit doc: SemanticDocument): List[String] = {
    symbol.info.map(_.signature) match {
      case Some(MethodSignature(_, parameters, _)) =>
        parameters.flatten.filterNot(_.isImplicit).map(_.displayName)
      case _ => Nil
    }
  }
}

case class NoUnnamedArgsConfig(minUnnamedArgs: Int)

object NoUnnamedArgsConfig {
  val default: NoUnnamedArgsConfig = NoUnnamedArgsConfig(5)

  implicit val surface: Surface[NoUnnamedArgsConfig] =
    metaconfig.generic.deriveSurface[NoUnnamedArgsConfig]

  implicit val decoder: ConfDecoder[NoUnnamedArgsConfig] =
    metaconfig.generic.deriveDecoder(default)
}

