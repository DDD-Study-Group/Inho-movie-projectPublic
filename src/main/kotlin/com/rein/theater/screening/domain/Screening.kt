package com.rein.theater.screening.domain

import com.rein.theater.discount.domain.value.Won
import com.rein.theater.movie.domain.PlayTime
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class Screening {
    val id: ID
    val ticketCount: Int
    private val price: Won
    
    constructor(title: String, startAt: LocalDateTime, time: PlayTime, ticketCount: Int, price: Int) {
        this.id = ID(title, startAt, time)
        this.ticketCount = ticketCount(ticketCount)
        this.price = price(price)    
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Screening
        return id == other.id
    }

    fun title(): String = id.title
    
    fun startAt(): LocalDateTime = id.startAt
    
    fun endAt(): LocalDateTime = id.endAt
    
    companion object {
        private const val MIN_PRICE = 1000
        private const val MIN_TICEKT_COUNT = 1
        
        private fun price(price: Int): Won = if (price < MIN_PRICE) throw InvalidPriceException(price) else Won(price)
        
        private fun ticketCount(count: Int): Int = if (count < MIN_TICEKT_COUNT) throw InvalidTicketCountException(count) else count
    }
}

data class ID(
    val title: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) {
    constructor(title: String, startAt: LocalDateTime, time: PlayTime) : this(title(title), startAt(startAt), endAt(startAt, time))
    
    companion object {
        private fun title(title: String): String = title.ifBlank { throw InvalidTitleException(title) }
        
        private fun startAt(startAt: LocalDateTime): LocalDateTime = 
            if (startAt.isBefore(LocalDateTime.now().plusHours(24)))
                throw InvalidStartAtException(startAt)
            else startAt

        private fun endAt(startAt: LocalDateTime, endAt: LocalDateTime): LocalDateTime =
            if (endAt.isBefore(startAt))
                throw InvalidEndAtException(startAt, endAt)
            else endAt
        
        private fun endAt(startAt: LocalDateTime, time: PlayTime): LocalDateTime = 
            endAt(startAt(startAt), startAt(startAt) + time)
    }
}

operator fun LocalDateTime.plus(playTime: PlayTime): LocalDateTime {
    val time = playTime.time
    return when(playTime.unit) {
        TimeUnit.HOURS -> this.plusHours(time)
        TimeUnit.MINUTES -> this.plusMinutes(time)
        else -> this.plusSeconds(time)
    }
}
