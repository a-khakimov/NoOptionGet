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

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See", pil = 73, rom = 100)

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See", pil = 73, rom = 100)

  val f: make_args_named.Foo = make_args_named.Foo.make

  f.foo1(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See", pil = 73, rom = 100)

  f.foo2(List(1, 2, 3), "See")

  make_args_named.Bar("Baz", Nil)

  def moo(bar: Int, baz: String, lee: List[Long], see: String, pil: Int)(implicit rom: Int, bom: String): Unit = { }
  implicit val rom: Int = 42
  implicit val bom: String = "Bom"
  moo(42, baz = "Baz", List(1, 2, 3), "See", 73)

  def hoo(
    bar: Int, baz: String, lee: List[Long], see: String, pil: Int, mel: make_args_named.Foo
  )(
    rom: Int, bom: String
  )(implicit vaa: Long): Unit = { }

  implicit val vaa: Long = 100

  hoo(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See", pil = 73, mel = f)(55, "bom")
}
