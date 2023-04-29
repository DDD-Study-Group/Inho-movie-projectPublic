package com.rein.theater.discount.application

import com.rein.theater.discount.application.domain.Discounts
import com.rein.theater.discount.domain.Discount
import org.springframework.stereotype.Service

@Service
class CreateDiscountService {
    fun create(discount: Discount) {
    }
    
    fun get(): Discounts {
        return Discounts()
    }

}
