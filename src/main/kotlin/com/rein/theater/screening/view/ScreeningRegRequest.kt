package com.rein.theater.screening.view

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.validation.constraints.Min

data class ScreeningRegRequest(
    @JsonProperty("movieID") @Min(1) val movieID: Long,
    @JsonProperty("startAt") val startAt: LocalDateTime,
    @JsonProperty("paidAmount") val paidAmount: Long,
    @JsonProperty("ticketCount") @Min(1) val ticketCount: Long
)
