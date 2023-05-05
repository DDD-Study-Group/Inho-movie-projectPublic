package com.rein.theater.discount

import com.google.gson.*
import com.ninjasquad.springmockk.MockkBean
import com.rein.theater.discount.application.CreateDiscountService
import com.rein.theater.discount.application.domain.Discounts
import com.rein.theater.discount.domain.*
import com.rein.theater.discount.domain.value.Won
import com.rein.theater.discount.view.*
import com.rein.theater.support.IntegrationTest
import io.mockk.every
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Stream

class DiscountControllerIntegrationTest : IntegrationTest() {
    @MockkBean(relaxed = true)
    private lateinit var service: CreateDiscountService
    
    @DisplayName("할인을 등록할 수 있다.")
    @ParameterizedTest
    @MethodSource("createSet")
    fun create(request: CreateDiscountRequest, expected: ResultMatcher) {
        mvc.perform(post("/discount").contentType(MediaType.APPLICATION_JSON)
                                                      .content(GSON.toJson(request)))
            .andExpect(expected)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("할인 순서가 0 이하이거나 할인 퍼센트가 0% 이하이거나 101% 이상 또는 할인 금액이 1000원 미만이면 할인을 등록할 수 없다")
    @ParameterizedTest
    @MethodSource("invalidRequest")
    fun create_when_invalid_request(request: String) {
        mvc.perform(post("/discount").contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("동일한 할인이 이미 등록되어 있다면 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("createSet")
    fun create_when_conflict() {
        every { service.create(any()) } throws AlreadyCreatedDiscountException()

        mvc.perform(post("/discount").contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(CreateDiscountRequest(
                DiscountConditionRequest(NOW.plusMonths(1), 3),
                DiscountPolicyRequest(PercentRequest(10), null)
            ))))
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andDo(MockMvcResultHandlers.print())
    }

    @DisplayName("순서조건의 날짜가 등록하려는 날짜보다 2일 이후가 아니라면 할인을 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidDateSet")
    fun create_when_invalid_date(date: LocalDate) {
        every { service.create(any()) } throws InvalidDiscountDateException(date)

        mvc.perform(post("/discount").contentType(MediaType.APPLICATION_JSON)
            .content(GSON.toJson(CreateDiscountRequest(
                DiscountConditionRequest(NOW.plusMonths(1), 3),
                DiscountPolicyRequest(PercentRequest(10), null)
            ))))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(jsonPath("$.code", equalTo(InvalidDiscountDateResponse.CODE)))
            .andExpect(jsonPath("$.message", equalTo("Invalid discount date. date=${date.format(DateTimeFormatter.ISO_DATE)}. Registration is available from ${NOW.plusDays(2).format(DateTimeFormatter.ISO_DATE)} onwards")))
            .andDo(MockMvcResultHandlers.print())
    }
    
    @DisplayName("전체 할인 목록을 조회할 수 있다.")
    @Test
    fun search() {
        val condition = Condition(NOW, 3)
        val policy = AmountPolicy(Won(3000))
        every { service.get() } returns Discounts(listOf(Discount(condition, policy)))
        
        mvc.perform(get("/discount"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.discounts.length()", equalTo(1)))
            .andExpect(jsonPath("$.discounts[0].condition.date", equalTo(condition.date.format(DateTimeFormatter.ISO_DATE))))
            .andExpect(jsonPath("$.discounts[0].condition.order", equalTo(condition.order)))
            .andExpect(jsonPath("$.discounts[0].policy.id", equalTo(policy.id().name)))
            .andExpect(jsonPath("$.discounts[0].policy.value", equalTo(policy.value())))
            .andDo(MockMvcResultHandlers.print())
    }

    companion object {
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd")
        private val NOW = LocalDate.now()
        private val GSON: Gson = with(GsonBuilder()){
            this.registerTypeAdapter(LocalDate::class.java, object : JsonSerializer<LocalDate> {
                override fun serialize(src: LocalDate?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
                    JsonPrimitive(DATE_FORMAT.format(src))
            })
            this.registerTypeAdapter(LocalDate::class.java, object : JsonDeserializer<LocalDate> {
                override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
                    return LocalDate.parse(json?.asString, DATE_FORMAT.withLocale(Locale.ENGLISH))
                }
            }).setPrettyPrinting().create()
        }
        
        @JvmStatic
        private fun invalidDateSet() = Stream.of(
            Arguments.arguments(NOW),
            Arguments.arguments(NOW.plusDays(1)),
            Arguments.arguments(NOW.plusMonths(1)),
            Arguments.arguments(NOW.plusYears(1)),
            Arguments.arguments(NOW.minusDays(1)),
            Arguments.arguments(NOW.minusMonths(1)),
            Arguments.arguments(NOW.minusYears(1))
        )
        
        @JvmStatic 
        private fun createSet() = Stream.of(
            Arguments.arguments(
                CreateDiscountRequest(
                    DiscountConditionRequest(NOW.plusMonths(1), 3),
                    DiscountPolicyRequest(PercentRequest(10), null)
                ),
                MockMvcResultMatchers.status().isCreated
            ),
            Arguments.arguments(
                CreateDiscountRequest(
                    DiscountConditionRequest(NOW.plusMonths(1), 1),
                    DiscountPolicyRequest(null, AmountRequest(3000))
                ),
                MockMvcResultMatchers.status().isCreated
            )
        )
        
        @JvmStatic
        private fun invalidRequest() = Stream.of(
            Arguments.arguments(
                "{\"discountCondition\": {\"date\": \"20230529\",\"order\": 3},\"discountPolicy\": {\"percent\": {\"value\": 0}}}"
            ),
            Arguments.arguments(
                "{\"discountCondition\": {\"date\": \"20230529\",\"order\": 3},\"discountPolicy\": {\"percent\": {\"value\": 101}}}"
            ),
            Arguments.arguments(
                "{\"discountCondition\": {\"date\": \"20230529\",\"order\": 3},\"discountPolicy\": {\"amount\": {\"value\": 0}}}"
            ),
            Arguments.arguments(
                "{\"discountCondition\": {\"date\": \"20230529\",\"order\": 3},\"discountPolicy\": {\"amount\": {\"value\": -1}}}"
            ),
            Arguments.arguments(
                "{\"discountCondition\": {\"date\": \"20230529\",\"order\": 0},\"discountPolicy\": {\"amount\": {\"value\": 1}}}"
            ),
            Arguments.arguments(
                "{\"discountCondition\": {\"date\": \"20230529\",\"order\": -1},\"discountPolicy\": {\"amount\": {\"value\": 1}}}"
            )
        )
    }
}
