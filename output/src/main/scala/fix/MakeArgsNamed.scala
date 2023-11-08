package fix

trait Foo {

  def foo1(bar: Int, baz: String, lee: List[Long], see: String): Unit

  def foo2(lee: List[Long], see: String): Unit
}

object Foo {

  def make: Foo = new Foo {

    override def foo1(bar: Int, baz: String, lee: List[Long], see: String): Unit = {}

    def foo2(lee: List[Long], see: String): Unit = {}
  }
}

case class Bar(baz: String, lee: List[Long])

class MakeArgsNamed {

  def foo(bar: Int, baz: String, lee: List[Long], see: String): Unit = { }

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See")

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See")

  val f: Foo = Foo.make

  f.foo1(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See")

  f.foo2(lee = List(1, 2, 3), see = "See")

  Bar("Baz", Nil)
}
