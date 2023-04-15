package com.rein.theater.screening

import com.rein.theater.screening.view.ScreeningRegRequest
import com.rein.theater.screening.view.ScreeningScheduleResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.LocalDate

@RestController
@RequestMapping("/screening")
class ScreeningController {
    @PostMapping(produces = ["application/json"])
    fun regist(@RequestBody request: ScreeningRegRequest): ResponseEntity<Any> {
        return ResponseEntity.created(URI("")).build()
    }
    
    @GetMapping(path = ["/{date}"])
    fun searchByDate(@PathVariable date: LocalDate): ResponseEntity<ScreeningScheduleResponse> {
        return ResponseEntity.ok(ScreeningScheduleResponse())
    }

    @GetMapping(path = ["/{movieID}"])
    fun searchByMovie(@PathVariable movieID: Long): ResponseEntity<ScreeningScheduleResponse> {
        return ResponseEntity.ok(ScreeningScheduleResponse())
    }
}
