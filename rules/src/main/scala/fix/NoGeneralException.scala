package fix

import fix.NoGeneralException._
import metaconfig.{ConfDecoder, Configured}
import metaconfig.generic.Surface
import scalafix.v1._

import scala.meta._

case class NoGeneralException(config: Config) extends SyntacticRule("NoGeneralException") {

  override def isLinter: Boolean = true

  override def description: String =
    """
      |The rule prohibits the use of the general exception type, encouraging the use of more specific exception types.
      |This improves the possibilities for error handling.
      |""".stripMargin

  def this() = this(NoGeneralException.defaultConfig)

  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect {
      case t @ Term.New(Init.After_4_6_0(Type.Name(tpe), _, _)) if config.forbidden.contains(tpe) =>
        Patch.lint(Diagnostic("", s"$tpe is not allowed", t.pos))
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

  private val forbidden = List("Exception", "Throwable")

  private val defaultConfig = Config(forbidden)

  implicit val surface: Surface[Config] =
    metaconfig.generic.deriveSurface[Config]

  implicit val decoder: ConfDecoder[Config] =
    metaconfig.generic.deriveDecoder(defaultConfig)
}
