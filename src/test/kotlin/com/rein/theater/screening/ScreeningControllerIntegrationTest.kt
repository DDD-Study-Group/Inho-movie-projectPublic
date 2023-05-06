package com.rein.theater.screening

import com.google.gson.*
import com.ninjasquad.springmockk.MockkBean
import com.rein.theater.discount.domain.value.Won
import com.rein.theater.screening.application.RegistScreeningService
import com.rein.theater.screening.application.SearchScreeningService
import com.rein.theater.screening.domain.AlreadyRegisteredScreeningException
import com.rein.theater.screening.domain.Screening
import com.rein.theater.screening.domain.ScreeningSchedule
import com.rein.theater.screening.view.RegistScreeningRequest
import com.rein.theater.support.IntegrationTest
import io.mockk.every
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Stream

class ScreeningControllerIntegrationTest : IntegrationTest() {
    @MockkBean(relaxed = true)
    private lateinit var register: RegistScreeningService
    @MockkBean(relaxed = true)
    private lateinit var scheduler: SearchScreeningService
    
    @DisplayName("상영 등록할 수 있다.")
    @Test
    fun regist() {
        mvc.perform(
            post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(RegistScreeningRequest(12L, NOW.plusMonths(1), 10000, 100))))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("영화 아이디 / 시작시간 / 영화 가격 / 티켓수량이 없다면 상영 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidRequest")
    fun regist_when_invalid_request(request: String) {
        mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("예매금액이 1000원 미만이거나 예매 티켓수가 1 미만이거나 상영 시간표에 이미 등록된 시간 / 상영시작시간이 등록시점보다 24시간 이전이 아니면 상영을 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidScreening")
    fun regist_when_invalid_screening(screening: String) {
        every { register.regist(any()) } throws IllegalArgumentException()
        
        mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(screening))
            .andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("상영 시간표에 이미 등록된 상영이 있다면 상영을 등록할 수 없다.")
    @Test
    fun regist_when_already_registered() {
        every { register.regist(any()) } throws AlreadyRegisteredScreeningException(NOW)

        mvc.perform(
            post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(RegistScreeningRequest(12L, NOW.plusMonths(1), 10000, 100))))
            .andExpect(status().isConflict)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("일별 상영 시간표를 조회할 수 있다.")
    @Test
    fun searchByDate() {
        val schedule = listOf(
            Screening("A", LocalTime.of(13, 10, 0), LocalTime.of(13, 10, 0).plusMinutes(300), 100, Won(13000))
        )
        every { scheduler.scheduleOn(any()) } returns ScreeningSchedule(schedule.toSet())
        
        mvc.perform(get("$BASE_URL/date/20230506"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.date", equalTo("20230506")))
            .andExpect(jsonPath("$.schedule.length()", equalTo(1)))
            .andExpect(jsonPath("$.schedule[0].order", equalTo(1)))
            .andExpect(jsonPath("$.schedule[0].title", equalTo(schedule[0].title)))
            .andExpect(jsonPath("$.schedule[0].startAt", equalTo(schedule[0].startAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")))))
            .andExpect(jsonPath("$.schedule[0].endAt", equalTo(schedule[0].endAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")))))
            .andExpect(jsonPath("$.schedule[0].ticketCount", equalTo(schedule[0].ticketCount)))
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("일별 상영 시간표가 없다면 조회할 수 없다.")
    @Test
    fun searchByDate_when_does_not_exist() {
        every { scheduler.scheduleOn(any()) } returns ScreeningSchedule(emptySet())

        mvc.perform(get("$BASE_URL/date/20230506"))
            .andExpect(status().isNotFound)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("영화별 상영 시간표를 조회할 수 있다.")
    @Test
    fun searchByMovie() {
        TODO("Not yet implemented")
    }

    @DisplayName("영화별 상영 시간표가 없다면 조회할 수 없다.")
    @Test
    fun searchByMovie_when_does_not_exist() {
        TODO("Not yet implemented")
    }
    
    companion object {
        private const val BASE_URL = "/screening"
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        private val GSON: Gson = with(GsonBuilder()){
            this.registerTypeAdapter(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime> {
                override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
                    JsonPrimitive(DATE_FORMAT.format(src))
            })
            this.registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
                override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
                    return LocalDateTime.parse(json?.asString, DATE_FORMAT.withLocale(Locale.ENGLISH))
                }
            }).setPrettyPrinting().create()
        }
        private val NOW = LocalDateTime.now()
        
        @JvmStatic
        private fun invalidRequest() = Stream.of(
            Arguments.arguments("{\"startAt\": \"2023-06-06T02:27:28\", \"price\": 1000, \"ticketCount\": 20}"),
            Arguments.arguments("{\"movieID\": 3, \"price\": 1000, \"ticketCount\": 20}"),
            Arguments.arguments("{\"movieID\": 3,\"startAt\": \"2023-06-06T02:27:28\", \"price\": 1000}"),
            Arguments.arguments("{}")
        )

        @JvmStatic
        private fun invalidScreening() = Stream.of(
            Arguments.arguments("{\"movieID\": 3,\"startAt\": \"${NOW.format(DATE_FORMAT)}\", \"price\": 1000, \"ticketCount\": 20}"),
            Arguments.arguments("{\"movieID\": 3,\"startAt\": \"${NOW.plusDays(1).format(DATE_FORMAT)}\", \"price\": 999, \"ticketCount\": 20}"),
            Arguments.arguments("{\"movieID\": 3,\"startAt\": \"${NOW.plusDays(1).format(DATE_FORMAT)}\", \"price\": 1000, \"ticketCount\": 0}")
        )
    }
}
