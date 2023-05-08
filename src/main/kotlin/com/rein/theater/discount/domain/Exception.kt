package com.rein.theater.discount.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlreadyCreatedDiscountException : RuntimeException()

class InvalidDiscountDateException(val date: LocalDate) : RuntimeException("Invalid discount date. date=${date.format(DateTimeFormatter.ISO_DATE)}")

class FailedToCreateDiscountException(condition: Condition, policy: Policy, unknown: Throwable) : RuntimeException("""
    Failed to create discount. condition(date=${condition.date.format(DateTimeFormatter.BASIC_ISO_DATE)}, order=${condition.order}), policy(id=${policy.id()}, value=${policy.value()})
""".trimIndent(), unknown)
