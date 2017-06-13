package com.github.mboogerd

import java.time.LocalDate

import com.github.mboogerd.BooleanValidators.{is18Years, isAlphabetic, isPostalCode}
import com.github.mboogerd.ErrorMessages.{notAPostalCode, notAValidAge, notAValidCountry, notAValidName}

import scalaz.{Failure, NonEmptyList, Success, Validation, ValidationNel}
import scalaz.Validation._
import scalaz.syntax.validation._
import scalaz.syntax.applicative._
import TestData._
/**
  *
  */
object ScalazPersonValidation extends App {

  // Validation
  def isValidName(s: String): ValidationNel[String, String] =
    if(isAlphabetic(s)) Success(s) else Failure(NonEmptyList(notAValidName))

  def isValidPostalCode(s: String): ValidationNel[String, String] =
    if(isPostalCode(s)) s.successNel else notAPostalCode.failureNel

  def isValidLegalDrivingCountry(country: String): ValidationNel[String, String] =
    if(isAlphabetic(country)) country.successNel else notAValidCountry.failureNel

  def isValidAge(birthDate: LocalDate): ValidationNel[String, LocalDate] =
    if(is18Years(birthDate)) birthDate.successNel else notAValidAge.failureNel


  def validatePerson(person: Person): Validation[NonEmptyList[String], Person] = {
    (isValidName(person.name) |@|
      isValidPostalCode(person.postalCode) |@|
      isValidLegalDrivingCountry(person.country) |@|
      isValidAge(person.birthDate))((_, _, _, _) ⇒ person)
  }

  println(validatePerson(validPerson))
  println(validatePerson(partiallyValidPerson))
  println(validatePerson(completelyInvalidPerson))
}
