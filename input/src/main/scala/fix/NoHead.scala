/*
rule = NoHead
*/
package fix

class NoHead {
  Seq(42).head /* assert: NoHead
  ^^^^^^^^^^^^
  Seq.head is not allowed
  */

  Nil.head /* assert: NoHead
  ^^^^^^^^
  List.head is not allowed
  */

  Seq().head /* assert: NoHead
  ^^^^^^^^^^
  Seq.head is not allowed
  */

  Set("A", "B").head /* assert: NoHead
  ^^^^^^^^^^^^^^^^^^
  Set.head is not allowed
  */

  val set = Set("A", "B")
  set.head /* assert: NoHead
  ^^^^^^^^
  Set.head is not allowed
  */

  val list = List("A", "B")
  list.head /* assert: NoHead
  ^^^^^^^^^
  List.head is not allowed
  */

  val seq = Seq("A", "B")
  seq.head /* assert: NoHead
  ^^^^^^^^
  Seq.head is not allowed
  */
}
