package com.rein.theater.movie.domain

import java.util.Objects
import java.util.concurrent.TimeUnit

class Movie constructor(val title: String, val time: PlayTime) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie
        return title == other.title
    }

    override fun hashCode(): Int = Objects.hash(title)
}

data class PlayTime(val time: Int, val unit: TimeUnit = TimeUnit.MINUTES)
