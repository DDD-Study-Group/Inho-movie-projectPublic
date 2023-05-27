package com.rein.theater.discount.domain

import com.rein.theater.discount.view.DiscountConditionRequest
import java.time.LocalDate

class Condition {
    val date: LocalDate
    val order: Int
    
    constructor(date: LocalDate, order: Int) {
        this.date = date
        this.order = order
    }
    
    constructor(request: DiscountConditionRequest) : this(request.date!!, request.order!!)
    
    fun match(date: LocalDate, order: Int): Boolean = this.date == date && this.order == order

    fun regist(): Condition {
        if (date.isBefore(LocalDate.now().plusDays(1))) throw InvalidDiscountDateException(date)
        if (order < 1) throw InvalidDiscountOrderException(order)

        return this
    }
}
