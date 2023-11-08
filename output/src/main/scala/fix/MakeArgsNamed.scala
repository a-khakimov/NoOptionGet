package fix

trait Foo {

  def foo1(bar: Int, baz: String, lee: List[Long], see: String, pil: Int): Unit

  def foo2(lee: List[Long], see: String): Unit
}

object Foo {

  def make: Foo = new Foo {

    override def foo1(bar: Int, baz: String, lee: List[Long], see: String, pil: Int): Unit = {}

    def foo2(lee: List[Long], see: String): Unit = {}
  }
}

case class Bar(baz: String, lee: List[Long])

class MakeArgsNamed {

  def foo(bar: Int, baz: String, lee: List[Long], see: String, pil: Int): Unit = { }

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See", pil = 73)

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See", pil = 73)

  val f: Foo = Foo.make

  f.foo1(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See", pil = 73)

  f.foo2(List(1, 2, 3), "See")

  Bar("Baz", Nil)
}
