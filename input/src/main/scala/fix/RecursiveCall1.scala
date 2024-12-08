package fix

/*
rule = NoRecursion
*/

class RecursiveCall1 {

  trait Ooo {
    def ooo(s: String)(implicit i: Int): Unit
  }

  class OooImpl extends Ooo {
    override def ooo(s: String)(implicit i: Int): Unit = qqq(s)

    def qqq(a: String)(implicit i: Int): Unit = www(a) /* assert: NoRecursion.DetectedRecursion
                                                ^^^^^^
    Recursion detected: OooImpl.www(String,Int) -> OooImpl.qqq(String,Int)
    */
    def www(b: String)(implicit i: Int): Unit = qqq(b)
  }
}
