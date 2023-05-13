package com.rein.theater.screening

import com.rein.theater.screening.application.RegistScreeningService
import com.rein.theater.screening.application.SearchScreeningService
import com.rein.theater.screening.view.DailyScheduleResponse
import com.rein.theater.screening.view.RegistScreeningRequest
import com.rein.theater.screening.view.ScheduleByMovieResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/screening")
class ScreeningController {
    @Autowired
    private lateinit var registScreeningService: RegistScreeningService
    @Autowired
    private lateinit var scheduler: SearchScreeningService
    
    @PostMapping(produces = ["application/json"])
    fun regist(@RequestBody request: RegistScreeningRequest): ResponseEntity<Any> {
        registScreeningService.regist(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
    
    @GetMapping(path = ["/date/{date}"])
    fun searchByDate(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") date: LocalDate): ResponseEntity<DailyScheduleResponse> {
        val schedule = scheduler.scheduleOn(date)
        return if (!schedule.exist()) ResponseEntity.notFound().build()
               else ResponseEntity.ok(DailyScheduleResponse(date, schedule))
    }

    @GetMapping(path = ["/movie/{movieID}"])
    fun searchByMovie(@PathVariable movieID: Long): ResponseEntity<ScheduleByMovieResponse> {
        return ResponseEntity.ok(ScheduleByMovieResponse())
    }
}
