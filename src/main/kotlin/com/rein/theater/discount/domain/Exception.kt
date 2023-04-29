package com.rein.theater.discount.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlreadyCreatedDiscountException : RuntimeException()

class InvalidDiscountDateException(val date: LocalDate) : RuntimeException("Invalid discount date. date=${date.format(DateTimeFormatter.ISO_DATE)}")
