package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Percent
import com.rein.theater.discount.domain.value.Won
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AlreadyCreatedDiscountException : RuntimeException()

abstract class InvalidDiscountArgumentException(message: String) : RuntimeException(message)

class InvalidDiscountDateException(val date: LocalDate) : InvalidDiscountArgumentException("Invalid discount date. date=${date.format(DateTimeFormatter.ISO_DATE)}")

class InvalidDiscountOrderException(order: Int) : InvalidDiscountArgumentException("Invalid discount order. order=$order")

class InvalidDiscountAmountException(discountAmount: Won) : InvalidDiscountArgumentException("Invalid discount amount. Discount amount is greater than ${AmountPolicy.MIN_DISCOUNT_AMOUNT} amount=${discountAmount.value}")

class InvalidDiscountPercentException(discountPercent: Int) : InvalidDiscountArgumentException("Invalid discount percent. Discount percent is greater than ${PercentPolicy.MIN_DISCOUNT_PERCENT.value} amount=$discountPercent") {
    constructor(discountPercent: Percent) : this(discountPercent.value)
}

class FailedToCreateDiscountException(condition: Condition, policy: Policy, unknown: Throwable) : RuntimeException("""
    Failed to create discount. condition(date=${condition.date.format(DateTimeFormatter.BASIC_ISO_DATE)}, order=${condition.order}), policy(id=${policy.id()}, value=${policy.value()})
""".trimIndent(), unknown)
