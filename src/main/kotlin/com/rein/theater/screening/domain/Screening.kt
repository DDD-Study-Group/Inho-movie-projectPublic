package com.rein.theater.screening.domain

import com.rein.theater.discount.domain.value.Won
import com.rein.theater.movie.domain.PlayTime
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/*class Screening(val title: String, val startAt: LocalTime, val endAt: LocalTime, val ticketCount: Int, val reserveAmount: Won) {
    constructor(title: String, startAt: LocalDateTime, playTime: PlayTime, ticketCount: Int, price: Int) : this(
        title, startAt.toLocalTime(), startAt.toLocalTime() + playTime, ticketCount, Won(price)
    )
}*/

class Screening {
    private val id: ID
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
    
    fun startAt(): LocalTime = id.startAt
    
    fun endAt(): LocalTime = id.endAt
    
    companion object {
        private const val MIN_PRICE = 1000
        private const val MIN_TICEKT_COUNT = 1
        
        private fun price(price: Int): Won = if (price < MIN_PRICE) throw InvalidPriceException(price) else Won(price)
        
        private fun ticketCount(count: Int): Int = if (count < MIN_TICEKT_COUNT) throw InvalidTicketCountException(count) else count
    }
}

class ID {
    val title: String
    val startAt: LocalTime
    val endAt: LocalTime
    
    constructor(title: String, startAt: LocalDateTime, time: PlayTime) {
        this.title = title(title)
        this.startAt = startAt(startAt)
        this.endAt = endAt(startAt, time)
    }
    
    companion object {
        private fun title(title: String): String = title.ifBlank { throw InvalidTitleException(title) }
        
        private fun startAt(startAt: LocalDateTime): LocalTime = 
            if (startAt.isBefore(LocalDateTime.now().plusHours(24)))
                throw InvalidStartAtException(startAt)
            else startAt.toLocalTime()

        private fun endAt(startAt: LocalTime, endAt: LocalTime): LocalTime =
            if (endAt.isBefore(startAt))
                throw InvalidEndAtException(startAt, endAt)
            else endAt
        
        private fun endAt(startAt: LocalDateTime, time: PlayTime): LocalTime = 
            endAt(startAt(startAt), startAt(startAt) + time)
    }
}

operator fun LocalTime.plus(playTime: PlayTime): LocalTime {
    val time = playTime.time.toLong()
    return when(playTime.unit) {
        TimeUnit.HOURS -> this.plusHours(time)
        TimeUnit.MINUTES -> this.plusMinutes(time)
        else -> this.plusSeconds(time)
    }
}
