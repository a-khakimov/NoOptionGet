/*
rule = NoUnnamedArgs
*/
package fix

package no_unnamed_args {

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

class NoUnnamedArgs {

  def foo(bar: Int, baz: String, lee: List[Long], see: String, pil: Int, rom: Int): Unit = { }

  foo(bar = 42, baz = "Baz", List(1, 2, 3), see = "See", pil =73, rom = 100) /* assert: NoUnnamedArgs
                             ^^^^^^^^^^^^^
  Unnamed arguments is not allowed - lee
  */

  foo(bar = 42, baz = "Baz", lee = List(1, 2, 3), "See", pil = 73, rom = 100) /* assert: NoUnnamedArgs
                                                  ^^^^^
  Unnamed arguments is not allowed - see
  */

  val f: no_unnamed_args.Foo = no_unnamed_args.Foo.make

  f.foo1(bar = 42, baz = "Baz", lee = List(1, 2, 3), see = "See", 73, rom = 100) /* assert: NoUnnamedArgs
                                                                  ^^
  Unnamed arguments is not allowed - pil
  */

  f.foo2(List(1, 2, 3), "See")

  no_unnamed_args.Bar("Baz", Nil)

  def moo(bar: Int, baz: String, lee: List[Long], see: String, pil: Int)(implicit rom: Int, bom: String): Unit = { }

  implicit val rom: Int = 100
  implicit val bom: String = "100"

  moo(42, "Baz", List(1, 2, 3), "See", 73)
}
