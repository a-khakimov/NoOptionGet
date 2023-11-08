package fix

import scalafix.v1._

import scala.meta._
import scala.meta.inputs.Position

class NoMapApply extends SemanticRule("NoMapApply") {

  private def patch(pos: Position) = {
    Patch.lint(Diagnostic("", "Map.apply is not allowed", pos))
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t@Term.Select(extractor: Term, Term.Name("apply")) if extractor.symbol.info
        .map(info => (info.displayName, info.signature.toString))
        .exists { case (name, signature) =>
          name.startsWith("Map") || signature.startsWith(s"Map[") ||
            signature.startsWith(s": Map[") || signature.startsWith(s"[K,V]: Map[")
        } => patch(t.pos)
      case t@Term.Apply.After_4_6_0(extractor: Term, _) if extractor.symbol.info
        .map(_.signature.toString)
        .exists { signature =>
          signature.startsWith(s": Map[") || signature.startsWith(s"Map[")
        } => patch(t.pos)
    }.asPatch
  }

  private val forbidden = "Map" :: "Map[" :: Nil
}
