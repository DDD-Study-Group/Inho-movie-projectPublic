package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Won
import java.time.LocalDate

class Discount(private val condition: Condition, val policy: DiscountPolicy) {
    fun date(): LocalDate = condition.date
    
    fun order(): Int = condition.order
    
    fun paidAmount(order: Int, reserveAmount: Won): Won = 
        if (condition.match(order)) policy.apply(reserveAmount)
        else reserveAmount
}
