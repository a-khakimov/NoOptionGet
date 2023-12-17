package fix

import scalafix.v1._
import scala.meta._

class NoOptionGet extends SemanticRule("NoOptionGet") {

  override def isLinter: Boolean = true

  override def description: String =
    """
      |This rule prohibits the use of the get method on Option to avoid a NoSuchElementException.
      |""".stripMargin

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t@Term.Select(extractor: Term, Term.Name("get")) if extractor.symbol.info
        .map(info => (info.displayName, info.signature.toString))
        .exists { case (name, signature) =>
          forbiddenNames.exists(name.contains) || forbiddenSignatures.exists(signature.startsWith)
        } => Patch.lint(Diagnostic("", "Option.get is not allowed", t.pos))
    }.asPatch
  }

  private val forbiddenSignatures = List("Option[", "Some[", "None", ": Option[", ": Some[", ": None")
  private val forbiddenNames = List("Option", "Some", "None")
}
