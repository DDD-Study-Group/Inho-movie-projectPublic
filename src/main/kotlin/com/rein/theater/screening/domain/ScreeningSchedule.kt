package com.rein.theater.screening.domain

class ScreeningSchedule(private val schedule: Set<Screening>) : Iterable<Screening> {
    fun exist(): Boolean = schedule.isNotEmpty()
    
    override fun iterator(): Iterator<Screening> = schedule.iterator()
}
