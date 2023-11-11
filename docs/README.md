# Pravila

This library contains [Scalafix](https://scalacenter.github.io/scalafix) rules for the code linting and rewriting. These rules help to correct common errors and bring the code to style conventions.

## Linting rules

### NoGeneralException

The rule prohibits the use of the general Exception type, encouraging the use of more specific exception types. This improves the possibilities for error handling.

```scala
for { 
  _ <- IO.fromOption(opt, new Exception("Boom!"))
/*
error: [NoGeneralException] Exception is not allowed
    _ <- IO.fromOption(opt, new Exception("Boom!"))
                            ^^^^^^^^^^^^^^^^^^^^^^
*/
```

### NoHead

This rule prohibits the use of `head` on collections in order to avoid a `NoSuchElementException`.


```scala
List(1, 2, 3).head
/*
error: [NoHead] List.head is not allowed
    List(1, 2, 3).head
    ^^^^^^^^^^^^^^^^^^
 */
```

### NoMapApply

This rule prohibits the use of `apply` on Map. The aim is to prevent a possible `NoSuchElementException` when the key is missing.


```scala
val map = Map[Int, String](1 -> "Foo")
map(1)
/*
[error] ... error:[NoMapApply] Map.apply is not allowed
[error] map(1)
[error] ^^^^^^
 */
```

### NoOptionGet

This rule prohibits the use of the `get` method on `Option` to avoid a `NoSuchElementException`.

```scala
val value = myOption.get
/*
[error] ... error: [NoOptionGet] Option.get is not allowed
[error]   val value = myOption.get
[error]               ^^^^^^^^^^^^
 */
```

### NoUnnamedArgs

This rule requires specifying arguments names when calling a function. It makes the code more readable.


```scala
function("Word", 42, 73.1)
/*
[error] ... error: [NoUnnamedArgs] Unnamed arguments is not allowed - word
[error]   function("Word", 42, 73.1)
[error]            ^^^^^^
[error] ... error: [NoUnnamedArgs] Unnamed arguments is not allowed - number
[error]   function("Word", 42, 73.1)
[error]                    ^^
[error] ... error: [NoUnnamedArgs] Unnamed arguments is not allowed - double
[error]   function("Word", 42, 73.1)
[error]                        ^^^^
 */
```

## Refactoring rules

### MakeArgsNamed

This rule converts passing of unnamed arguments in functions to named arguments to improve readability.

Was:

```scala
foo(10, "bar")
```
Became:

```scala
foo(number = 10, word = "bar")
```
