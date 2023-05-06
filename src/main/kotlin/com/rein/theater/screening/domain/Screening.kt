package com.rein.theater.screening.domain

import com.rein.theater.discount.domain.value.Won
import com.rein.theater.movie.domain.Movie
import com.rein.theater.movie.domain.PlayTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class Screening(val title: String, val startAt: LocalTime, val endAt: LocalTime, val ticketCount: Int, val reserveAmount: Won) {
    constructor(movie: Movie, startAt: LocalTime, ticketCount: Int, reserveAmount: Won) : this(
        movie.title,
        startAt,
        startAt + movie.time,
        ticketCount, reserveAmount
    )
}

data class ID(val title: String, val startAt: LocalTime, val endAt: LocalTime)

operator fun LocalTime.plus(playTime: PlayTime): LocalTime {
    val time = playTime.time.toLong()
    return when(playTime.unit) {
        TimeUnit.HOURS -> this.plusHours(time)
        TimeUnit.MINUTES -> this.plusMinutes(time)
        else -> this.plusSeconds(time)
    }
}
