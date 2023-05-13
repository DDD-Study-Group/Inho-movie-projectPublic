package com.rein.theater.movie.view

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class MovieCreateRequest {
    val title: String
    val playTime: Long
    
    @JsonCreator constructor(
        @JsonProperty("title", required = true) title: String?,
        @JsonProperty("playTime", required = true) playTime: Long?
    ) {
        this.title = title!!
        this.playTime = playTime!!
    }
}
