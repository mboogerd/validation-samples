package com.github.mboogerd

import com.github.mboogerd.ErrorMessages.notAPostalCode

import scalaz.ValidationNel
import scalaz.syntax.applicative._
import scalaz.syntax.validation._

/**
  *
  */
class ScalazLocationValidation {

  case class RawLocation(cityname: String, postcode: String)

  sealed trait City
  case object Amsterdam extends City
  case object Utrecht extends City

  case class Postcode(region: String, street: String)

  case class Location(name: City, postcode: Postcode)


  def validateCity(name: String): ValidationNel[String, City] = name match {
    case "Amsterdam" ⇒ Amsterdam.successNel
    case "Utrecht" ⇒ Utrecht.successNel
    case other ⇒ s"$other is not a valid city".failureNel
  }

  val postalCodeMatch = "^([0-9]{4})([A-Z]{2})$".r
  def validPostalcode(code: String): ValidationNel[String, Postcode] = code match {
    case postalCodeMatch(region, city) ⇒ Postcode(region, city).successNel
    case _ ⇒ notAPostalCode.failureNel
  }

  def validateLocation(input: RawLocation): ValidationNel[String, Location] =
    (validateCity(input.cityname) |@| validPostalcode(input.postcode))((ci, pc) ⇒ Location(ci, pc))

  println(validateLocation(RawLocation("Amsterdam", "1234AB")))
  println(validateLocation(RawLocation("Amsterdam", "1234ABC")))
  println(validateLocation(RawLocation("Rotterdam", "1234AB")))
  println(validateLocation(RawLocation("Rotterdam", "1234ABC")))
}

object ScalazLocationValidationApp extends ScalazLocationValidation with App