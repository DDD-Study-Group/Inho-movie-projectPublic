package com.rein.theater.discount.view

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class SearchDiscountsResponse(
    @JsonProperty("discounts") val discounts: List<SearchDiscountResponse>
)

data class SearchDiscountResponse(
    @JsonProperty("discountID") val discountID: Long,
    @JsonProperty("condition") val condition: SearchDiscountConditionResponse,
    @JsonProperty("policy") val policy: SearchDiscountPolicyResponse,
)

data class SearchDiscountConditionResponse (
    @JsonProperty("id") val id: Long,
    @JsonProperty("dateTime") val dateTime: LocalDateTime,
    @JsonProperty("order") val order: Int
)

abstract class SearchDiscountPolicyResponse(
    @JsonProperty("id") val id: Long,
)

class SearchDiscountAmountResponse(
    id: Long,
    @JsonProperty("amount") val amount: Long
) : SearchDiscountPolicyResponse(id)

class SearchDiscountPercentResponse(
    id: Long,
    @JsonProperty("percent") val percent: Int
) : SearchDiscountPolicyResponse(id)


