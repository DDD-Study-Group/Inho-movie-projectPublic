package com.rein.theater.discount.domain

import com.rein.theater.discount.view.DiscountConditionRequest
import java.time.LocalDate

class Condition(val date: LocalDate, val order: Int) {
    
    constructor(request: DiscountConditionRequest) : this(request.date!!, request.order!!)
    
    fun match(order: Int): Boolean = this.order == order
}
