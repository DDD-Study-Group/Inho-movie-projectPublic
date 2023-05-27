package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Won
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class Discount(val condition: Condition, val policy: Policy) {
    
    fun id(): Int = Objects.hash(condition.date.format(DateTimeFormatter.BASIC_ISO_DATE), condition.order)
    
    fun date(): LocalDate = condition.date
    
    fun order(): Int = condition.order

    fun regist(): Discount = Discount(condition.regist(), policy.regist())
    
    fun paidAmount(order: Int, reserveAmount: Won): Won = 
        if (condition.match(order)) policy.apply(reserveAmount)
        else reserveAmount

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Discount
        return id() == other.id()
    }
}
