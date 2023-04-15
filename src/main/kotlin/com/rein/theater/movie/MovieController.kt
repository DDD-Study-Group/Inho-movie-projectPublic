package com.rein.theater.movie

import com.rein.theater.movie.view.MovieCreateRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/movie")
class MovieController {
    
    @PostMapping(produces = ["application/json"])
    fun regist(@RequestBody request: MovieCreateRequest): ResponseEntity<Any> {
        
        return ResponseEntity.created(URI("")).build()
    }
}
