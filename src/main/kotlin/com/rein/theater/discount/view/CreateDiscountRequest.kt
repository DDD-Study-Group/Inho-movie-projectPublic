package com.rein.theater.discount.view

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.rein.theater.discount.domain.*
import com.rein.theater.discount.domain.value.Percent
import com.rein.theater.discount.domain.value.Won
import java.time.LocalDate
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class CreateDiscountRequest (
    @JsonProperty("discountCondition") 
    val discountCondition: DiscountConditionRequest,
    @JsonProperty("discountPolicy") 
    val discountPolicy: DiscountPolicyRequest
) {
    fun discount(): Discount = Discount(
        Condition(discountCondition),
        discountPolicy.create()
    )
}

data class DiscountConditionRequest (
    @JsonProperty("date", required = true) 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "UTC")
    val date: LocalDate?,
    @JsonProperty("order", required = true) 
    val order: Int? = null
)

data class DiscountPolicyRequest (
    @JsonProperty("percent") 
    @field: Min(1) @field: Max(100) 
    val percent: PercentRequest?,
    @JsonProperty("amount") 
    @field: Min(1000L) 
    val amount: AmountRequest?
) {
    fun create(): DiscountPolicy = 
        if (percent != null) PercentPolicy(Percent(percent.value))
        else if (amount != null) AmountPolicy(Won(amount.value))
        else throw IllegalArgumentException("Percent or amount must be exist. percent=$percent, amount=$amount")
}

class PercentRequest(value: Int) {
    val value: Int
    
    init {
        if (value < 1 || value > 100) throw IllegalArgumentException()
        this.value = value
    }
}

class AmountRequest(value: Int) {
    val value: Int
    
    init {
        if (value < 1000) throw IllegalArgumentException()
        this.value = value
    }
}
