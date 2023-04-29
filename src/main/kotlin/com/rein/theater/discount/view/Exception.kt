package com.rein.theater.discount.view

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class InvalidDiscountDateResponse @JsonCreator private constructor(
    @field:JsonProperty("code") val code: Int,
    @field:JsonProperty("message") val message: String,
) {
    constructor(date: LocalDate) : this(
        CODE,
        "Invalid discount date. date=${date.format(DateTimeFormatter.ISO_DATE)}. Registration is available from ${LocalDate.now().plusDays(2).format(DateTimeFormatter.ISO_DATE)} onwards"
    )
    
    companion object {
        const val CODE = 400001
    }
}
