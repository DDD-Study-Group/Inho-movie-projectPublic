package com.rein.theater.ticketing.view

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min

data class ReserveTicketRequest(
    @JsonProperty("screeningID") val screeningID: Long,
    @JsonProperty("ticketCount") @Min(1) val ticketCount: Long,
    @JsonProperty("payment") val payment: String
)

