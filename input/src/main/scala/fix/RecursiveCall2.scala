package fix

/*
rule = NoRecursion
*/

class RecursiveCall2 {

  def mmm(): Unit = mmm() /* assert: NoRecursion.DetectedRecursion
                    ^^^^^
  Recursion detected: RecursiveCall2.mmm()
  */
}
