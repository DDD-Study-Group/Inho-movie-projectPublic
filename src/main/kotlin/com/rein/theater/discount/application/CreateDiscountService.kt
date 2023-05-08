package com.rein.theater.discount.application

import com.rein.theater.discount.application.domain.Discounts
import com.rein.theater.discount.domain.Discount
import com.rein.theater.discount.domain.DiscountRepository
import com.rein.theater.discount.domain.FailedToCreateDiscountException
import org.springframework.stereotype.Service

@Service
class CreateDiscountService(private val repository: DiscountRepository) {
    fun create(discount: Discount): Discount = try { 
        repository.save(discount) 
    } catch (unknown: Exception) {
        throw FailedToCreateDiscountException(discount.condition, discount.policy, unknown)
    }
    
    fun get(): Discounts {
        return Discounts()
    }

}
