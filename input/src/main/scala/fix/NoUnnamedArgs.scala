/*
rule = NoUnnamedArgs
*/
package fix

class NoUnnamedArgs {

  def foo(bar: Int, baz: String, lee: List[Long], see: String): Unit = { }

  foo(42, "Baz", List(1, 2, 3), "See")

  foo(42, baz = "Baz", List(1, 2, 3), "See")

  final case class Foo(bar: Int, baz: String, lee: List[Long], see: String)

  Foo(42, "Baz", List(1, 2, 3), "See")
}
