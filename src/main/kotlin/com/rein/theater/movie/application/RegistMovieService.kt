package com.rein.theater.movie.application

import com.rein.theater.movie.domain.Movies
import com.rein.theater.movie.view.MovieCreateRequest
import org.springframework.stereotype.Service

@Service
class RegistMovieService {
    fun regist(request: MovieCreateRequest) {
        
    }

    fun getAll(): Movies = Movies()
}
