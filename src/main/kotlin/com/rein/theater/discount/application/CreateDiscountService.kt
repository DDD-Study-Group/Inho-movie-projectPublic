package com.rein.theater.discount.application

import com.rein.theater.discount.application.domain.Discounts
import com.rein.theater.discount.domain.Discount
import com.rein.theater.discount.domain.DiscountRepository
import com.rein.theater.discount.domain.FailedToCreateDiscountException
import com.rein.theater.screening.domain.AlreadyRegisteredScreeningException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CreateDiscountService @Autowired constructor(private val repository: DiscountRepository) {
    private val logger = LoggerFactory.getLogger(CreateDiscountService::class.java)
    fun create(discount: Discount): Discount = try { 
        // TODO 중복 판단 AlreadyCreatedDiscountException
        // TODO InvalidDiscountDateException
        repository.save(discount) 
    } catch (arse: AlreadyRegisteredScreeningException) {
        logger.error(arse.message)
        throw arse
    } catch (unknown: Exception) {
        throw FailedToCreateDiscountException(discount.condition, discount.policy, unknown)
    }
    
    fun get(): Discounts {
        return Discounts()
    }

}
