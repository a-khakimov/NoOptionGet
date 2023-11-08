/*
rule = NoMapApply
*/
package fix

class NoMapApply {

  Map.empty[Int, String].apply(42) /* assert: NoMapApply
  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  Map.apply is not allowed
  */

  val map = Map.empty[Int, String]
  map.apply(42) /* assert: NoMapApply
  ^^^^^^^^^
  Map.apply is not allowed
  */

  map(42) /* assert: NoMapApply
  ^^^^^^^
  Map.apply is not allowed
  */

  val map2 = Map(42 -> "Foo")
  map2(42) /* assert: NoMapApply
  ^^^^^^^^
  Map.apply is not allowed
  */

  case class ConcurrentMap[Key, Value](key: Key, value: Value) {
    def apply(key: Key): Value = value
  }

  val cm = ConcurrentMap[Int, String](42, "Foo")
  cm(42)
}
