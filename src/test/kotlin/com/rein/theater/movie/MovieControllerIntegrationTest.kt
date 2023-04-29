package com.rein.theater.movie

import com.google.gson.Gson
import com.rein.theater.movie.view.MovieCreateRequest
import com.rein.theater.support.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MovieControllerIntegrationTest : IntegrationTest() {
    @Autowired
    private lateinit var cut: MovieController

    @Test
    fun regist() {
        val request = Gson().toJson(MovieCreateRequest("title", 120))
        mvc.perform(post("/movie").contentType(MediaType.APPLICATION_JSON)
                                             .content(request))
            .andExpect(status().isCreated)
    }
}
