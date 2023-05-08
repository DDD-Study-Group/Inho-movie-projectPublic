package com.rein.theater.discount

import com.rein.theater.discount.application.CreateDiscountService
import com.rein.theater.discount.view.CreateDiscountRequest
import com.rein.theater.discount.view.SearchDiscountsResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/discount")
@Validated
class DiscountController {
    @Autowired
    private lateinit var service: CreateDiscountService
    
    @PostMapping(path = [""])
    fun create(@Valid @RequestBody request: CreateDiscountRequest): ResponseEntity<Any> {
        val discount = service.create(request.discount())
        return ResponseEntity.created(URI("/discount/${discount.id()}")).build()
    }
    
    @GetMapping()
    fun search(): ResponseEntity<SearchDiscountsResponse> {
        return ResponseEntity.ok(SearchDiscountsResponse(service.get()))
    }
}
