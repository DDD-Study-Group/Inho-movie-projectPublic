package com.rein.theater.movie.domain

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MovieRepository {
    fun findById(id: Long): Optional<Movie> = Optional.empty()
    
    fun save(movie: Movie) = movie
}
