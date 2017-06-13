package com.github.mboogerd

import com.github.mboogerd.BooleanValidators.{is18Years, isLegalDrivingCountry, isName, isPostalCode}
import com.github.mboogerd.TestData._

/**
  *
  */
object BooleanValidation extends App {

  def isLegalDrivingPerson(person: Person): Boolean = {
    import person._
    isName(name) && isPostalCode(postalCode) && is18Years(birthDate) && isLegalDrivingCountry(country)
  }

  println(isLegalDrivingPerson(validPerson))
  println(isLegalDrivingPerson(partiallyValidPerson))
  println(isLegalDrivingPerson(completelyInvalidPerson))
}
