package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Won
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class DiscountRepository {
    fun save(discount: Discount): Discount = Discount(
        Condition(LocalDate.now(), 1),
        AmountPolicy(Won(10000))
    )
}
