package com.github.mboogerd

import scalaz.std.anyVal._
import scalaz.std.list._
import scalaz.syntax.traverse._
import scalaz.syntax.validation._
import scalaz.{NonEmptyList, ValidationNel}

/**
  *
  */
class ScalazValidationComposition extends ScalazLocationValidation {

  // Constrain existing validating functions
  def validateUtrecht(name: String): ValidationNel[String, City] = validateCity(name).ensure(NonEmptyList("is not Utrecht!"))(_ == Utrecht)

  def validateNotBlacklisted(postcode: String) = validPostalcode(postcode).excepting {
    case Postcode("1000", "AB") ⇒ "Cannot accept 1000AB"
    case Postcode("1000", "AC") ⇒ "Cannot accept 1000AC"
  }

  println("\nBLACKLISTED POSTCODES")
  println(validateNotBlacklisted("1000AB"))
  println(validateNotBlacklisted("1000AA"))


  // Use a conjunction/disjunction-like composition of validations
  def validatePrice(price: Int): ValidationNel[String, Int] =
    if (price > 0) price.successNel else s"$price is a price that is zero or lower".failureNel

  println("\nVALIDATING PRODUCT PRICES")
  println(validatePrice(1) +++ validatePrice(-1) +++ validatePrice(2) +++ validatePrice(-2))
  println(validatePrice(1) +|+ validatePrice(-1) +|+ validatePrice(2) +|+ validatePrice(-2))

  // Using traverse
  val validList1: ValidationNel[String, List[Int]] = List(1, 2, 3).traverseU(validatePrice)
  val validList2: ValidationNel[String, List[Int]] = List(4, 5, 6, 7).traverseU(validatePrice)
  val invalidList: ValidationNel[String, List[Int]] = List(4, -5, -6, 7).traverseU(validatePrice)

  println("\nVALIDATING PRODUCT PRICE-LISTS")
  println(validList1 +++ invalidList +++ validList2)
  println(validList1 +|+ invalidList +|+ validList2)
  println(validList1 +++ invalidList +|+ validList2)
}


object ScalazProductValidationApp extends ScalazValidationComposition with App