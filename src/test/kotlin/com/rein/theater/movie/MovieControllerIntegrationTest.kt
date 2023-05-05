package com.rein.theater.movie

import com.google.gson.Gson
import com.ninjasquad.springmockk.MockkBean
import com.rein.theater.movie.application.RegistMovieService
import com.rein.theater.movie.domain.AlreadyRegisteredMovieException
import com.rein.theater.movie.domain.Movie
import com.rein.theater.movie.domain.Movies
import com.rein.theater.movie.domain.PlayTime
import com.rein.theater.movie.view.MovieCreateRequest
import com.rein.theater.support.IntegrationTest
import io.mockk.every
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.concurrent.TimeUnit
import java.util.stream.Stream

class MovieControllerIntegrationTest : IntegrationTest() {
    @MockkBean(relaxed = true)
    private lateinit var service: RegistMovieService

    @DisplayName("영화를 등록할 수 있다.")
    @Test
    fun regist() {
        val request = Gson().toJson(MovieCreateRequest("title", 120))
        mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(status().isCreated)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("제목 또는 플레이시간이 없다면 영화를 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidRequestSet")
    fun regist_when_invalid_request(movie: String) {
        mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
            .content(movie))
            .andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("제목 또는 플레이시간이 유효하지 않다면 영화를 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidMovieSet")
    fun regist_when_invalid_movie(movie: String) {
        mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
            .content(movie))
            .andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("이미 등록된 영화를 등록할 수 없다.")
    @Test
    fun regist_when_already_registered() {
        every { service.regist(any()) } throws AlreadyRegisteredMovieException()
        
        mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
            .content(Gson().toJson(MovieCreateRequest("title", 120))))
            .andExpect(status().isConflict)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("등록된 영화목록을 조회할 수 있다.")
    @Test
    fun search() {
        val movies = listOf(Movie("A", PlayTime(10)), Movie("B", PlayTime(3, TimeUnit.HOURS)))
        every { service.getAll() } returns Movies(movies.toSet())
        
        mvc.perform(get(BASE_URL))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.movies.length()", equalTo(movies.size)))
            .andExpect(jsonPath("$.movies[0].title", equalTo(movies[0].title)))
            .andExpect(jsonPath("$.movies[0].playTime.time", equalTo(movies[0].time.time)))
            .andExpect(jsonPath("$.movies[0].playTime.unit", equalTo(movies[0].time.unit.name)))
            .andExpect(jsonPath("$.movies[1].title", equalTo(movies[1].title)))
            .andExpect(jsonPath("$.movies[1].playTime.time", equalTo(movies[1].time.time)))
            .andExpect(jsonPath("$.movies[1].playTime.unit", equalTo(movies[1].time.unit.name)))
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("등록된 영화목록이 없다면 조회할 수 없다.")
    @Test
    fun search_when_does_not_exist() {
        every { service.getAll() } returns Movies()

        mvc.perform(get(BASE_URL)).andExpect(status().isNotFound).andDo(MockMvcResultHandlers.print())
    }

    companion object {
        private const val BASE_URL = "/movie"
        
        @JvmStatic
        private fun invalidRequestSet() = Stream.of(
            Arguments.arguments("{\"playTime\": 50}"),
            Arguments.arguments("{\"title\": \"타이타닉\"}"),
            Arguments.arguments("{\"title\": \"\",\"playTime\": 50}"),
            Arguments.arguments("{\"title\": \"타이타닉\",\"playTime\": 0}"),
            Arguments.arguments("{\"title\": \"타이타닉\",\"playTime\": -1}")
        )

        @JvmStatic
        private fun invalidMovieSet() = Stream.of(
            Arguments.arguments("{\"title\": \"타이타닉\",\"playTime\": 0}"),
            Arguments.arguments("{\"title\": \"타이타닉\",\"playTime\": -1}")
        )
    }
}
