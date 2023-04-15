package com.rein.theater.discount

import com.rein.theater.discount.view.CreateDiscountRequest
import com.rein.theater.discount.view.CreateDiscountSuccessResponse
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/discount")
class DiscountController {
    @PostMapping(path = ["/create"])
    fun create(@RequestBody request: CreateDiscountRequest): ResponseEntity<CreateDiscountSuccessResponse> {
        return ResponseEntity.ok(CreateDiscountSuccessResponse())
    }
}
