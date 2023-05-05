package com.rein.theater.movie

import com.rein.theater.movie.application.RegistMovieService
import com.rein.theater.movie.view.MovieCreateRequest
import com.rein.theater.movie.view.SearchMovieResponse
import com.rein.theater.movie.view.SearchMoviesResponse
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
    @Autowired
    private lateinit var service: RegistMovieService
    
    @GetMapping
    fun search(): ResponseEntity<*> {
        val movies = service.getAll()
        if (!movies.exist()) return ResponseEntity.notFound().build<Any>()
        return ResponseEntity.ok(SearchMoviesResponse(movies.map { SearchMovieResponse(it.title, it.time) }.toSet()))
    }
    
    @PostMapping(produces = ["application/json"])
    fun regist(@RequestBody request: MovieCreateRequest): ResponseEntity<Any> {
        service.regist(request)
        return ResponseEntity.created(URI("")).build()
    }
}
