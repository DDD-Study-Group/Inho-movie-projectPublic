package com.rein.theater.movie.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Objects
import java.util.concurrent.TimeUnit

class Movie {
    val title: String
    val time: PlayTime
    
    @JsonCreator
    constructor(@JsonProperty("title") title: String, @JsonProperty("time") time: PlayTime) {
        this.title = title.ifEmpty { throw IllegalArgumentException() }
        this.time = time
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie
        return title == other.title
    }

    override fun hashCode(): Int = Objects.hash(title)
}

class PlayTime {
    val time: Long
    val unit: TimeUnit
    
    constructor(time: Long, unit: TimeUnit = TimeUnit.MINUTES) {
        this.time = if (time < 1) throw IllegalArgumentException() else time
        this.unit = unit
    }
    
    companion object {
        fun minutes(minutes: Int): PlayTime = PlayTime(minutes.toLong(), TimeUnit.MINUTES)
        fun minutes(minutes: Long): PlayTime = PlayTime(minutes, TimeUnit.MINUTES)
    }
}
