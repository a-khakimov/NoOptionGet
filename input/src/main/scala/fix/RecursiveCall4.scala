package fix

import scala.annotation.tailrec

/*
rule = NoRecursion
*/

class RecursiveCall4 {

  def calc = {
    def factorial(n: Int): Int = {
      @tailrec def factorialAcc(acc: Int, n: Int): Int = { /* assert: NoRecursion.DetectedRecursion
      ^
      Recursion detected: RecursiveCall4.calc().factorialAcc(Int,Int)
      */
        if (n <= 1) acc
        else factorialAcc(n * acc, n - 1)
      }

      factorialAcc(1, n)
    }

    factorial(1)
  }
}
