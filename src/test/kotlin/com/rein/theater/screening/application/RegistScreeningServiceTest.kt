package com.rein.theater.screening.application

import com.rein.theater.movie.domain.Movie
import com.rein.theater.movie.domain.MovieRepository
import com.rein.theater.movie.domain.PlayTime
import com.rein.theater.screening.domain.*
import com.rein.theater.screening.view.RegistScreeningRequest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream

@ExtendWith(MockKExtension::class)
class RegistScreeningServiceTest {
    @RelaxedMockK
    private lateinit var screeningRepository: ScreeningRepository
    @RelaxedMockK
    private lateinit var movieRepository: MovieRepository
    @InjectMockKs
    private lateinit var cut: RegistScreeningService
    
    @Test
    fun regist() {
        val request = request(NOW.plusDays(3), 10000, 100)
        val expected = screening(MOVIE.title, request.startAt, MOVIE.time, request.ticketCount, request.price)
        every { movieRepository.findById(request.movieID) } returns Optional.of(MOVIE)

        with(cut.regist(request)) { assertThat(this).isEqualTo(expected) }
    }

    @DisplayName("시작시간이 현재시간 기준 24시간 이후가 아니거나 예매금액이 1000원 미만이거나 예매 티켓수가 1장 미만이라면 상영등록 할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidArgs")
    fun regist_when_invalid_parameters(request: RegistScreeningRequest, expected: Class<InvalidScreeningArgumentException>) {
        every { movieRepository.findById(request.movieID) } returns Optional.of(MOVIE)
        
        assertThatThrownBy { cut.regist(request) }.isExactlyInstanceOf(expected)
    }
    
    @DisplayName("등록되지 않은 영화라면 상영등록 할 수 없다.")
    @Test
    fun regist_when_does_not_exist_movie() {
        val request = request(NOW.plusDays(3), 10000, 100)
        screening(MOVIE.title, request.startAt, MOVIE.time, request.ticketCount, request.price)
        every { movieRepository.findById(allAny()) } returns Optional.empty()

        assertThatThrownBy { cut.regist(request) }.isExactlyInstanceOf(NotExistMovieException::class.java)
    }

    @DisplayName("이미 등록된 상영이 있다면 상영을 등록할 수 없다.")
    @Test
    fun regist_when_already_exist() {
        val request = request(NOW.plusDays(3), 10000, 100)
        every { movieRepository.findById(allAny()) } returns Optional.of(Movie("title", PlayTime.minutes(120)))
        every { screeningRepository.save(allAny()) } throws AlreadyRegisteredScreeningException(mockk<ID>())
        
        assertThatThrownBy { cut.regist(request) }.isExactlyInstanceOf(AlreadyRegisteredScreeningException::class.java)
    }

    @Test
    fun regist_when_occured_unknown_error() {
        val request = request(NOW.plusDays(3), 10000, 100)
        every { movieRepository.findById(allAny()) } returns Optional.of(MOVIE)
        every { screeningRepository.save(allAny()) } throws Exception()
        
        assertThatThrownBy { cut.regist(request) }.isExactlyInstanceOf(FailedToRegistScreeningException::class.java)
    }
    
    private fun screening(title: String, startAt: LocalDateTime, time: PlayTime, ticketCount: Int, price: Int): Screening {
        val screening = Screening(title, startAt, time, ticketCount, price)
        every { screeningRepository.save(allAny()) } returns screening
        return screening
    }
    
    companion object {
        private val NOW = LocalDateTime.now()
        private val MOVIE = Movie("title", PlayTime.minutes(120))
        
        @JvmStatic
        private fun invalidArgs() = Stream.of(
            Arguments.arguments(
                request(NOW.plusHours(23), 10000, 100),
                InvalidStartAtException::class.java
            ),
            Arguments.arguments(
                request(NOW.plusHours(25), 999, 100),
                InvalidPriceException::class.java
            ),
            Arguments.arguments(
                request(NOW.plusHours(25), 1000, 0),
                InvalidTicketCountException::class.java
            )
        )
        
        private fun request(startAt: LocalDateTime, price: Int, ticketCount: Int) = RegistScreeningRequest(123L, startAt, price, ticketCount)
    }
}
