package com.rein.theater.screening.application

import com.rein.theater.screening.domain.ScreeningSchedule
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service    
class SearchScreeningService {
    fun scheduleOn(date: LocalDate): ScreeningSchedule {
        return ScreeningSchedule(setOf())
    }
}
