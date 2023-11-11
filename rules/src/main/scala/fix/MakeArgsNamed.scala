package fix

import metaconfig.{ConfDecoder, Configured}
import metaconfig.generic.Surface
import scalafix.v1._

import scala.meta._

case class MakeArgsNamed(config: MakeArgsNamedConfig) extends SemanticRule("MakeArgsNamed") {

  def this() = this(MakeArgsNamedConfig.default)

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case fun @ Term.Apply.After_4_6_0(_, args) =>
        getParameters(fun.symbol).get(args.size).map {
          parameters => if (parameters.size > config.minArgs) {
            args.values.zip(parameters).map {
              case (arg, name) if !arg.symbol.info.exists(_.isParameter) =>
                Patch.addLeft(arg, s"$name = ")
              case _ => Patch.empty
            }
          } else Nil
        }.getOrElse(Nil)
    }
  }.flatten.asPatch

  import MakeArgsNamedConfig.decoder

  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf.getOrElse("MakeArgsNamed")(
      MakeArgsNamedConfig.default
    ).map(MakeArgsNamed)
  }

  // TODO: Make it better!
  private def getParameters(symbol: Symbol)(implicit doc: SemanticDocument): Map[Int, List[String]] = {
    symbol.info.map(_.signature) match {
      case Some(MethodSignature(_, parameters, _)) =>
        parameters
          .map(params => params.size -> params.map(_.displayName))
          .toMap
      case Some(ClassSignature(_, _, _, declarations)) =>
        declarations
          .filterNot(decl => decl.isConstructor && decl.isPrimary)
          .map(_.signature match {
            case MethodSignature(_, parameters :: _, _) => parameters.map(_.displayName)
            case _ => Nil
          })
          .map(params => params.size -> params)
          .toMap
      case _ => Map.empty
    }
  }
}

case class MakeArgsNamedConfig(minArgs: Int)

object MakeArgsNamedConfig {
  val default: MakeArgsNamedConfig = MakeArgsNamedConfig(5)

  implicit val surface: Surface[MakeArgsNamedConfig] =
    metaconfig.generic.deriveSurface[MakeArgsNamedConfig]

  implicit val decoder: ConfDecoder[MakeArgsNamedConfig] =
    metaconfig.generic.deriveDecoder(default)
}
