/*
rule = MakeArgsNamed
*/
package fix

package make_args_named {

  trait Foo {

    def foo1(bar: Int, baz: String, lee: List[Long], see: String, pil: Int, rom: Int): Unit

    def foo2(lee: List[Long], see: String): Unit
  }

  object Foo {

    def make: Foo = new Foo {

      override def foo1(bar: Int, baz: String, lee: List[Long], see: String, pil: Int, rom: Int): Unit = {}

      def foo2(lee: List[Long], see: String): Unit = {}
    }
  }

  case class Bar(baz: String, lee: List[Long])

}

class MakeArgsNamed {

  def foo(bar: Int, baz: String, lee: List[Long], see: String, pil: Int, rom: Int): Unit = { }

  foo(42, "Baz", List(1, 2, 3), "See", 73, 100)

  foo(42, baz = "Baz", List(1, 2, 3), "See", 73, 100)

  val f: make_args_named.Foo = make_args_named.Foo.make

  f.foo1(42, baz = "Baz", List(1, 2, 3), "See", 73, 100)

  f.foo2(List(1, 2, 3), "See")

  make_args_named.Bar("Baz", Nil)
}