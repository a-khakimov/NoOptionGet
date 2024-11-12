package fix

/*
rule = NoRecursion
*/

class RecursiveCall5 {

  trait Ooo {
    def ooo(): Unit
  }

  trait Vvv {
    def ooo(): Unit
  }

  class OooImpl(vvv: Vvv) extends Ooo {
    override def ooo(): Unit = vvv.ooo()
  }
}
