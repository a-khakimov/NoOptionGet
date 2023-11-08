/*
rule = MakeArgsNamed
*/
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

  foo(42, "Baz", List(1, 2, 3), "See")

  foo(42, baz = "Baz", List(1, 2, 3), "See")

  val f: Foo = Foo.make

  f.foo1(42, baz = "Baz", List(1, 2, 3), "See")

  f.foo2(List(1, 2, 3), "See")

  Bar("Baz", Nil)
}
