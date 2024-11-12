package fix

/*
rule = NoRecursion
*/

class RecursiveCall0 {

  def foo(): Unit = baz()
  def bar(): Unit = foo()
  def baz(): Unit = bar() /* assert: NoRecursion.DetectedRecursion
                    ^^^^^
  Recursion detected: RecursiveCall0.bar() -> RecursiveCall0.foo() -> RecursiveCall0.baz()
  */
}
