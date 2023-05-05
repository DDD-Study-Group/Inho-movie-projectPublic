package com.rein.theater.movie.view

import com.fasterxml.jackson.annotation.JsonProperty

data class MovieCreateRequest(
    @field:JsonProperty("title", required = true) 
    val title: String, 
    @field:JsonProperty("playTime", required = true)
    val playTime: Long
)
