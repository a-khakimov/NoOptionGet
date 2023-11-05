package fix

import scalafix.v1._
import scala.meta._

class NoOptionGet extends SemanticRule("NoOptionGet") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t@Term.Select(extractor: Term, name: Term.Name) if name.value == "get" &&
        extractor.symbol.info.get.signature.toString.contains("Option[") =>
        println(s"--> ${extractor.symbol.info.get.signature.toString}")
        Patch.lint(Diagnostic("", "Option.get is not allowed", t.pos))
    }.asPatch
  }
}
