package com.rein.theater.discount.view

import com.fasterxml.jackson.annotation.JsonProperty
import com.rein.theater.discount.application.domain.Discounts
import com.rein.theater.discount.domain.DiscountPolicy
import java.time.LocalDate

class SearchDiscountsResponse {
    @JsonProperty("discounts") 
    val discounts: List<SearchDiscountResponse>
    
    constructor(discounts: Discounts) {
        this.discounts = discounts.map {
            SearchDiscountResponse(
                SearchDiscountConditionResponse(it.date(), it.order()),
                SearchDiscountPolicyResponse(it.policy)
            )
        }
    }
}

data class SearchDiscountResponse(
    @JsonProperty("condition") val condition: SearchDiscountConditionResponse,
    @JsonProperty("policy") val policy: SearchDiscountPolicyResponse,
)

data class SearchDiscountConditionResponse (
    @JsonProperty("date") val date: LocalDate,
    @JsonProperty("order") val order: Int
)

class SearchDiscountPolicyResponse constructor(
    @field: JsonProperty("id") val id: String,
    @field: JsonProperty("value") val value: Number
) {
    constructor(policy: DiscountPolicy) : this(policy.id().name, policy.value())
}
