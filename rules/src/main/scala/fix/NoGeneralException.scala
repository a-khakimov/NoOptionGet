package fix

import fix.NoGeneralException._
import metaconfig.{ConfDecoder, Configured}
import metaconfig.generic.Surface
import scalafix.v1._

import scala.meta._

case class NoGeneralException(config: Config) extends SemanticRule("NoGeneralException") {

  def this() = this(NoGeneralException.defaultConfig)

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t@Term.New(_) => {
        t.symbol.info.map(_.displayName) match {
          case Some(exception) if config.forbidden.contains(exception) =>
            Patch.lint(Diagnostic("", "Exception is not allowed", t.pos))
          case _ => Patch.empty
        }
      }
    }.asPatch
  }

  override def withConfiguration(config: Configuration): Configured[Rule] = {
    config.conf.getOrElse("NoGeneralException")(defaultConfig).map(
      config => NoGeneralException(config.copy(forbidden = forbidden ++ config.forbidden))
    )
  }
}

object NoGeneralException {

  case class Config(forbidden: List[String])

  private val forbidden = List(
    "Exception",
    "Throwable"
  )

  private val defaultConfig = Config(forbidden)

  implicit val surface: Surface[Config] =
    metaconfig.generic.deriveSurface[Config]

  implicit val decoder: ConfDecoder[Config] =
    metaconfig.generic.deriveDecoder(defaultConfig)
}
