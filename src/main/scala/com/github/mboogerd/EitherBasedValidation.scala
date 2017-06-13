package com.github.mboogerd

import com.github.mboogerd.BooleanValidators.{is18Years, isLegalDrivingCountry, isName, isPostalCode}
import com.github.mboogerd.ErrorMessages.{notAPostalCode, notAValidAge, notAValidCountry, notAValidName}
import com.github.mboogerd.TestData.{completelyInvalidPerson, validPerson, partiallyValidPerson}

/**
  *
  */
object EitherBasedValidation extends App {

  def isLegalDrivingPersonEitherSimple(person: Person): Either[String, Person] = {
    import person._
    if(isName(name) && isPostalCode(postalCode) && is18Years(birthDate) && isLegalDrivingCountry(country))
      Right(person)
    else
      Left("Not a valid driving person")
  }

  println("\nSIMPLE")
  println(isLegalDrivingPersonEitherSimple(validPerson))
  println(isLegalDrivingPersonEitherSimple(partiallyValidPerson))
  println(isLegalDrivingPersonEitherSimple(completelyInvalidPerson))


  def isLegalDrivingPersonEitherDetailed(person: Person): Either[String, Person] = {
    import person._
    if(isName(name)) {
      if(isPostalCode(postalCode)) {
        if(is18Years(birthDate)) {
          if(isLegalDrivingCountry(country)) {
            Right(person)
          } else
            Left(notAValidCountry)
        } else
          Left(notAValidAge)
      } else
        Left(notAPostalCode)
    } else
      Left(notAValidName)
  }

  println("\nDETAILED")
  println(isLegalDrivingPersonEitherDetailed(validPerson))
  println(isLegalDrivingPersonEitherDetailed(partiallyValidPerson))
  println(isLegalDrivingPersonEitherDetailed(completelyInvalidPerson))


  def isLegalDrivingPersonEitherMonadic(person: Person): Either[String, Person] = {
    import person._
    for {
      _ ← if(isName(name)) Right(person) else Left(notAValidName)
      _ ← if(isPostalCode(postalCode)) Right(person) else Left(notAPostalCode)
      _ ← if(is18Years(birthDate)) Right(person) else Left(notAValidAge)
      _ ← if(isLegalDrivingCountry(country)) Right(person) else Left(notAValidCountry)
    } yield person
  }

  println("\nMONADIC")
  println(isLegalDrivingPersonEitherMonadic(validPerson))
  println(isLegalDrivingPersonEitherMonadic(partiallyValidPerson))
  println(isLegalDrivingPersonEitherMonadic(completelyInvalidPerson))
}
