/*
rule = NoOptionGet
*/
package fix

object NoOptionGet {
  val option: Option[Int] = Some(42)
  option.get /* assert: NoOptionGet
  ^^^^^^^^^^
  Option.get is not allowed
  */
}
