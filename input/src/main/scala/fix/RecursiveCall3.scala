package fix

/*
rule = NoRecursion
*/

class RecursiveCall3 {

  trait Ggg {
    def ggg: Unit
  }

  class GggImpl extends Ggg {
    override def ggg: Unit = eee()

    def eee(): Unit = println("Hello, eee")
    def foo(): Unit = println("Hello, foo")
  }
}
