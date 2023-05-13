package com.rein.theater.screening.application

import com.rein.theater.movie.domain.Movie
import com.rein.theater.movie.domain.MovieRepository
import com.rein.theater.screening.domain.InvalidScreeningArgumentException
import com.rein.theater.screening.domain.Screening
import com.rein.theater.screening.domain.ScreeningRepository
import com.rein.theater.screening.view.RegistScreeningRequest
import org.springframework.stereotype.Service

@Service
class RegistScreeningService constructor(
    private val screeningRepository: ScreeningRepository,
    private val movieRepository: MovieRepository
) {
    fun regist(request: RegistScreeningRequest): Screening = try {
        val movie = findMovie(request.movieID)
        screeningRepository.save(
            Screening(
                movie.title, request.startAt, movie.time, request.ticketCount, request.price
            )
        )
    } catch (isa: InvalidScreeningArgumentException) {
        throw isa
    } catch(nee: NotExistMovieException) {
        throw nee
    } catch (unknown: Exception) {
        throw FailedToRegistScreeningException(request)
    }

    private fun findMovie(id: Long): Movie = movieRepository.findById(id).orElseThrow { NotExistMovieException(id) }
}
