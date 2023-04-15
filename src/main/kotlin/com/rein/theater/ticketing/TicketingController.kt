package com.rein.theater.ticketing

import com.rein.theater.ticketing.view.ReserveTicketRequest
import com.rein.theater.ticketing.view.ReserveTicketSuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ticketing")
class TicketingController {
    @PostMapping(path = ["/reserve"])
    fun reserve(@RequestBody request: ReserveTicketRequest): ResponseEntity<ReserveTicketSuccessResponse> {
        return ResponseEntity.ok(ReserveTicketSuccessResponse())
    }
    
    @DeleteMapping(path = ["/cancel/{ticketingID}"])
    fun cancel(@PathVariable ticketingID: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}
