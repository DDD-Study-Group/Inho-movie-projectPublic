package com.rein.theater.discount

import com.rein.theater.discount.view.CreateDiscountRequest
import com.rein.theater.discount.view.SearchDiscountsResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/discount")
class DiscountController {
    @PostMapping(path = ["/create"])
    fun create(@RequestBody request: CreateDiscountRequest): ResponseEntity<Any> {
        return ResponseEntity.created(URI("")).build()
    }
    
    @GetMapping()
    fun search(): ResponseEntity<SearchDiscountsResponse> {
        return ResponseEntity.ok(SearchDiscountsResponse(emptyList()))
    }
    
    @PutMapping(path = ["/{discountID}"])
    fun apply(@PathVariable discountID: Long): ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}
