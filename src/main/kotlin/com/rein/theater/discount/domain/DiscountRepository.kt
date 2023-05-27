package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Won
import com.rein.theater.screening.domain.AlreadyRegisteredScreeningException
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class DiscountRepository {
    fun save(discount: Discount): Discount = try {
        Discount(
            Condition(LocalDate.now(), 1),
            AmountPolicy(Won(10000))
        )
    } catch (dke: DuplicateKeyException) {
        throw AlreadyRegisteredScreeningException(discount)
    }
}
