package fix

import scalafix.v1._
import scala.meta._

class NoHead extends SemanticRule("NoHead") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t@Term.Select(extractor: Term, name: Term.Name) if name.value == "head"
        && checkFor(extractor, "List") => Patch.lint(Diagnostic("", "List.head is not allowed", t.pos))
      case t@Term.Select(extractor: Term, name: Term.Name) if name.value == "head"
        && checkFor(extractor, "Seq") => Patch.lint(Diagnostic("", "Seq.head is not allowed", t.pos))
      case t@Term.Select(extractor: Term, name: Term.Name) if name.value == "head"
        && checkFor(extractor, "Set") => Patch.lint(Diagnostic("", "Set.head is not allowed", t.pos))
    }.asPatch
  }

  private def checkFor(extractor: Term, tpe: String)(implicit doc: SemanticDocument): Boolean = {
    extractor.symbol.info
      .map(info => (info.displayName, info.signature.toString))
      .exists { case (name, signature) =>
        name.contains(tpe) || name.contains("Nil") || signature.contains(s"$tpe[")
      }
  }
}
