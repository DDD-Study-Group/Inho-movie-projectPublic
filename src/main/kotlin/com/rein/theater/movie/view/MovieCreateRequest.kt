package com.rein.theater.movie.view

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min

data class MovieCreateRequest(
    @JsonProperty("title") 
    val title: String, 
    @JsonProperty("playTime") @Min(value = 1) 
    val playTime: Long
)
