package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Percent
import com.rein.theater.discount.domain.value.Won

enum class PolicyID {
    PERCENT_DISCOUNT, AMOUNT_DISCOUNT
}

abstract class Policy {
    abstract fun apply(reserveAmount: Won): Won

    abstract fun id(): PolicyID

    abstract fun value(): Number
    
    companion object {
        fun of(amount: Won): Policy = AmountPolicy(amount)
        
        fun of(percent: Percent): Policy = PercentPolicy(percent)
    }
}

class AmountPolicy(private val discountAmount: Won) : Policy() {
    override fun apply(reserveAmount: Won): Won = reserveAmount - discountAmount
    
    override fun id() = PolicyID.AMOUNT_DISCOUNT

    override fun value() = discountAmount.value
}

class PercentPolicy(private val discountPercent: Percent) : Policy() {
    override fun apply(reserveAmount: Won): Won = reserveAmount - reserveAmount * discountPercent
    
    override fun id() = PolicyID.PERCENT_DISCOUNT

    override fun value() = discountPercent.value
}
