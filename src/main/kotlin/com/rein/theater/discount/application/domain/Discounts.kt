package com.rein.theater.discount.application.domain

import com.rein.theater.discount.domain.Discount

class Discounts(private val discounts: List<Discount> = emptyList()) : Iterable<Discount> {
    override fun iterator(): Iterator<Discount> = discounts.iterator()
}
