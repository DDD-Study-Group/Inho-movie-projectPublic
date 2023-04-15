package com.rein.theater.discount.view

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.validation.constraints.Min

data class CreateDiscountRequest(
    @JsonProperty("discountCondition") val discountCondition: DiscountConditionRequest,
    @JsonProperty("discountPolicy") val discountPolicy: DiscountPolicyRequest
)

data class DiscountConditionRequest(
    @JsonProperty("dateTime") val dateTime: LocalDateTime,
    @JsonProperty("order") @Min(1) val order: Int
)

data class DiscountPolicyRequest(
    @JsonProperty("discountPercent") val discountPercent: Int?,
    @JsonProperty("discountAmount") val discountAmount: Int?
)
