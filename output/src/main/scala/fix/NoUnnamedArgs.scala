package fix

class NoUnnamedArgs {

  def foo(bar: Int, baz: String, lee: List[Long], see: String): Unit = { }

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See")

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See")

  final case class Foo(bar: Int, baz: String, lee: List[Long], see: String)

  Foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See")
}
