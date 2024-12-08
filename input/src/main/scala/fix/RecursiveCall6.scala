package fix

/*
rule = NoRecursion
*/

class RecursiveCall6 {

  trait Handler[T] {
    def handle(): Unit = println("foo")
  }

  class FooHandler extends Handler[FooHandler] {
    override def handle(): Unit = {
      super.handle()
      println("bar")
    }
  }
}
