package learnshapeless

import shapeless._

import Data._

/** General Guidelines
  *
  * Definitons named `eg` are examples to learn from. Examples are numbered by the exercises they related to or inform.
  *
  * Exercises beginning with `ex` are for you to complete. Each exercise has a comment above describing the task.
  *
  * Note: the file is runnable, so you can drop in println statements to look at the values of expressions
  *
  * */

object HLists extends App {



  /* Example 1: An HList consisting of "Einstein", the Int value 1879 and the Country Germany */
  def eg1_einstein: String :: Int :: Country :: HNil = "Einstein" :: 1879 :: Germany :: HNil

  /*Exercise 1: Construct an HList consisting of "Newton", the Int value 1642 and the Country England.

    Exercise 2: Give it an the explicit type. This isn't typically necessary in application code,
    inference is preferred, but is a good learning exercise.

    Some options to see the type of `ex_newton` printed out:
    (a) start the `sbt console`, and enter `:type learnshapeless.HLists.ex_newton`
    (b) if you are using Intellij, you can focus cursor on the expression and activate TypeInfo command (Ctrl-Shift-P on my Mac)
     */
  def ex_newton = "Newton" :: 1642 :: England :: HNil

  import shapeless.syntax.std.tuple._
  def eg_tuple_prepend_append: (String, String, Int, Country, Discovery) =
    ("Albert" +: eg1_einstein).tupled :+ TheoryOfRelativity

  /* Exercise 3: Prepend the String "Isaac" to `ex_newton` */
  def ex_prepend = "Isaac" +: ex_newton

  /* Convert `ex_newton` into a tuple */
  def ex_tuple: (String, Int, Country) = ex_newton.tupled

  /* Using operations available via `import syntax.std.tuple._`, append `Calculus` to `ex_tuple`  */
  def ex_tuple_append = ex_tuple :+ Calculus

  def eg_from_tuple: HList = eg_tuple_prepend_append.productElements

  /* convert ex_tuple_append into an HList */
  def ex_from_tuple = ex_tuple_append.productElements


  /* Example: Mapping over an HList. Each type `T` in the list should be handled with an `at[T]` expression.
  * A */
  trait DefaultIdentityMapping extends Poly1 {
    implicit def default[T] = at[T](x => x)
  }
  object ExamplePoly extends DefaultIdentityMapping {
    implicit def yearsSinceBirth = at[Int](2016 - _)
    implicit def isAustralian[C <: Country] = at[C](_ == Australia)
  }

  def eg_poly = eg1_einstein.map(ExamplePoly)

  /* Apply a Poly1 mapping over `ex_newton` that converts the name to ALLCAPS, and leaves other fields unchanged
   * Return resulting HList */
  object AllCapsPoly extends DefaultIdentityMapping {
    implicit def allCaps = at[String](_.map(_.toUpper))
  }
  def ex_poly = ex_newton.map(AllCapsPoly)

  /* Try writing a Poly1 mapping like `isAustralian` above, that doesn't use a type parameter `C <: Country`. Instead
   * directly define an `at[Country]` clause. Leave other fields unchanged.
   * Apply your new mapping over `ex_newton` and return the result */
  object IsAustralianPoly extends DefaultIdentityMapping {
    implicit def isAustralia = at[Country](_ == Australia)
  }
  def ex_poly_country = ex_newton.map(IsAustralianPoly)

  def eg_index: Country = eg1_einstein(2)

  /* Extract the 3rd element of `ex_poly_country` using a index.
  Does the result surprise you? Why did it happen this way?

  A: The 3rd element is the country England, not 'false'. So the Country element hasnt been mapped.
  The reason is that the at[Country] case in `IsAustralianPoly` matches type Country exactly,
  but not subtypes like England. */
  def ex_poly_country_element_3rd = ex_poly_country(2)
  print(ex_poly_country_element_3rd)

  def eg_mapped_by_index = eg1_einstein.updateAtWith(2)(_ == Australia)

  /* Transform the surname field in `ex_newton` to the first name by looking it up in `Data.scientistsFirstNames`.
  * Use updateAtWith to identify the field by numeric index rather than y type.
  * `updateAtWith` returns the old value, and the updated HList in a tuple */
  def ex_to_firstname_by_index: (String, HList) = ex_newton.updateAtWith(0)(Data.scientistsFirstNames)

}
