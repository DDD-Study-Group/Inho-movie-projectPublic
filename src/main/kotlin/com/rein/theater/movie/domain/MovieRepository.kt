package com.rein.theater.movie.domain

import org.springframework.stereotype.Repository
import java.util.*

@Repository
abstract class MovieRepository {
    abstract fun findById(id: Long): Optional<Movie>
}
