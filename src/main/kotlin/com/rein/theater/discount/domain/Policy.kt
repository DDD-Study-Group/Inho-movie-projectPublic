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
    
    abstract fun regist(): Policy
    
    companion object {
        fun of(amount: Won): Policy = AmountPolicy(amount)
        
        fun of(percent: Percent): Policy = PercentPolicy(percent)
    }
}

class AmountPolicy(private val discountAmount: Won) : Policy() {
    override fun apply(reserveAmount: Won): Won = reserveAmount - discountAmount
    
    override fun id() = PolicyID.AMOUNT_DISCOUNT

    override fun value() = discountAmount.value
    
    override fun regist(): Policy {
        if (discountAmount < MIN_DISCOUNT_AMOUNT) throw InvalidDiscountAmountException(discountAmount)
        return this
    }
    
    companion object {
        val MIN_DISCOUNT_AMOUNT = Won(1000)
    }
}

class PercentPolicy : Policy {
    private val discountPercent: Percent
    
    constructor(discountPercent: Percent) {
        this.discountPercent = discountPercent
    }
    
    constructor(percent: Int) {
        this.discountPercent = try {
            Percent(percent)   
        } catch (e: IllegalArgumentException) {
            throw InvalidDiscountPercentException(percent)
        }
    }
    
    override fun apply(reserveAmount: Won): Won = reserveAmount - reserveAmount * discountPercent
    
    override fun id() = PolicyID.PERCENT_DISCOUNT

    override fun value() = discountPercent.value
    
    override fun regist(): Policy {
        if (discountPercent < MIN_DISCOUNT_PERCENT) throw InvalidDiscountPercentException(discountPercent)
        return this
    }
    
    companion object {
        val MIN_DISCOUNT_PERCENT = Percent(1)
    }
}
