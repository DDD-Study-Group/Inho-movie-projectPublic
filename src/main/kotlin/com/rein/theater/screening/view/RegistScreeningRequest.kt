package com.rein.theater.screening.view

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.validation.constraints.Min

data class RegistScreeningRequest @JsonCreator constructor(
    @JsonProperty("movieID", required = true) @Min(1) 
    val movieID: Long,
    
    @JsonProperty("startAt", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    val startAt: LocalDateTime,
    
    @JsonProperty("price", required = true) @Min(1000) 
    val price: Long,
    
    @JsonProperty("ticketCount", required = true) @Min(1) 
    val ticketCount: Long
)
