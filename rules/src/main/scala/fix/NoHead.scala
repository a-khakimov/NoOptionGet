package fix

import scalafix.v1._
import scala.meta._

class NoHead extends SemanticRule("NoHead") {

  override def isLinter: Boolean = true

  override def description: String =
    """
      |This rule prohibits the use of head on collections in order to avoid a NoSuchElementException.
      |""".stripMargin

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t@Term.Select(extractor: Term, Term.Name("head"))
        if checkFor(extractor, "List") => Patch.lint(Diagnostic("", "List.head is not allowed", t.pos))
      case t@Term.Select(extractor: Term, Term.Name("head"))
        if checkFor(extractor, "Seq") => Patch.lint(Diagnostic("", "Seq.head is not allowed", t.pos))
      case t@Term.Select(extractor: Term, Term.Name("head"))
        if checkFor(extractor, "Set") => Patch.lint(Diagnostic("", "Set.head is not allowed", t.pos))
    }.asPatch
  }

  private def checkFor(extractor: Term, tpe: String)(implicit doc: SemanticDocument): Boolean = {
    extractor.symbol.info
      .map(info => (info.displayName, info.signature.toString))
      .exists { case (name, signature) =>
        name.startsWith(tpe) || name.contains("Nil") ||
          signature.startsWith(s": $tpe[") || signature.startsWith(s"$tpe[")
      }
  }
}
