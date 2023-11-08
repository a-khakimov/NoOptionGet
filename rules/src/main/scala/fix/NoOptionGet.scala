package fix

import scalafix.v1._
import scala.meta._

class NoOptionGet extends SemanticRule("NoOptionGet") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t@Term.Select(extractor: Term, Term.Name("get")) if extractor.symbol.info
        .map(info => (info.displayName, info.signature.toString))
        .exists { case (name, signature) =>
          name.contains("Option") ||
          name.contains("Some") ||
          name.contains("None") ||
          signature.contains("Option[") ||
          signature.contains("Some[") ||
          signature.contains("None")
        } => Patch.lint(Diagnostic("", "Option.get is not allowed", t.pos))
    }.asPatch
  }
}
