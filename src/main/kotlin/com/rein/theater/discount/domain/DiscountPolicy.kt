package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Percent
import com.rein.theater.discount.domain.value.Won

enum class DiscountPolicyID {
    PERCENT_DISCOUNT, AMOUNT_DISCOUNT
}

interface DiscountPolicy {
    fun apply(reserveAmount: Won): Won
    
    fun id(): DiscountPolicyID
    
    fun value(): Number
}

class AmountPolicy(private val discountAmount: Won) : DiscountPolicy {
    override fun apply(reserveAmount: Won): Won = reserveAmount - discountAmount
    
    override fun id() = DiscountPolicyID.AMOUNT_DISCOUNT

    override fun value() = discountAmount.value
}

class PercentPolicy(private val discountPercent: Percent) : DiscountPolicy {
    override fun apply(reserveAmount: Won): Won = reserveAmount - reserveAmount * discountPercent
    
    override fun id() = DiscountPolicyID.PERCENT_DISCOUNT

    override fun value() = discountPercent.value
}
