package com.rein.theater.movie.application

import com.rein.theater.movie.domain.Movie
import com.rein.theater.movie.domain.MovieRepository
import com.rein.theater.movie.domain.Movies
import com.rein.theater.movie.domain.PlayTime
import com.rein.theater.movie.view.MovieCreateRequest
import org.springframework.stereotype.Service

@Service
class RegistMovieService(private val repository: MovieRepository) {
    fun regist(request: MovieCreateRequest) {
        repository.save(Movie(
            request.title, PlayTime.minutes(request.playTime)
        ))
    }

    fun getAll(): Movies = Movies()
}
