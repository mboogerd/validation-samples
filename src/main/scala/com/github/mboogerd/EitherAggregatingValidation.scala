package com.github.mboogerd

import com.github.mboogerd.BooleanValidators._
import com.github.mboogerd.ErrorMessages._
import com.github.mboogerd.TestData._

/**
  *
  */
object EitherAggregatingValidation extends App {

  def isLegalDrivingPersonEitherAggregating(person: Person): Either[List[String], Person] = {
    def error(flag: Boolean)(error: ⇒ String): List[String] = if (!flag) List(error) else Nil

    import person._
    val errors =
      error(isName(name))(notAValidName) ++
        error(isPostalCode(postalCode))(notAPostalCode) ++
        error(is18Years(birthDate))(notAValidAge) ++
        error(isLegalDrivingCountry(country))(notAValidCountry)

    if (errors.isEmpty) Right(person)
    else Left(errors)
  }

  println(isLegalDrivingPersonEitherAggregating(validPerson))
  println(isLegalDrivingPersonEitherAggregating(partiallyValidPerson))
  println(isLegalDrivingPersonEitherAggregating(completelyInvalidPerson))

}
