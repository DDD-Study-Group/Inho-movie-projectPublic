package com.rein.theater.screening.view

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.rein.theater.screening.domain.Screening
import com.rein.theater.screening.domain.ScreeningSchedule
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalTime

class DailyScheduleResponse {
    @JsonProperty("date") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd") val date: LocalDate
    @JsonProperty("schedule") val schedule: Set<SearchScreeningResponse>
    
    constructor(date: LocalDate, schedule: ScreeningSchedule) {
        this.date = date
        this.schedule = schedule.mapIndexed { index, screening ->
            SearchScreeningResponse(index+1, screening)   
        }.toSet()
    }
}

class SearchScreeningResponse {
    @JsonProperty("order") val order: Int
    @JsonProperty("title") val title: String
    @JsonProperty("startAt") @DateTimeFormat(pattern = "HH:mm:ss") val startAt: LocalTime
    @JsonProperty("endAt") @DateTimeFormat(pattern = "HH:mm:ss") val endAt: LocalTime
    @JsonProperty("ticketCount") val ticketCount: Int
    
    constructor(order: Int, screening: Screening) {
        this.order = order
        this.title = screening.title
        this.startAt = screening.startAt
        this.endAt = screening.endAt
        this.ticketCount = screening.ticketCount
    }
}
