package com.rein.theater.screening.domain

import com.rein.theater.discount.domain.value.Won
import com.rein.theater.movie.domain.PlayTime
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.util.stream.Stream

@DisplayName("상영 Domain 테스트")
class ScreeningTest {
    @DisplayName("상영을 등록할 수 있다.")
    @Test
    fun create() {
        with(screening()) {
            assertThat(this.title()).isEqualTo(TITLE)       
            assertThat(this.startAt()).isEqualTo(START_AT)       
            assertThat(this.endAt()).isEqualTo(START_AT + TIME)       
            assertThat(this.ticketCount).isEqualTo(ticketCount) 
            assertThat(this.price).isEqualTo(PRICE)
        }
    }

    @DisplayName("상영 시작시간이 등록일보다 24시간 이후가 아니거나 티켓수가 1장 미만이거나 예매금액이 1000원 미만이면 상영을 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidScreenings")
    fun create_when_invalid_screening(startAt: LocalDateTime, ticketCount: Int, price: Won, expected: Class<InvalidScreeningArgumentException>) {
        assertThatThrownBy { 
            screening(startAt = startAt, ticketCount = ticketCount, price = price)
        }.isExactlyInstanceOf(expected)
    }
    
    companion object {
        private const val TITLE = "title"
        private val START_AT = LocalDateTime.now().plusDays(2)
        private val TIME = PlayTime(120)
        private const val TICKET_COUNT = 100
        private val PRICE = Won(10000)
        
        @JvmStatic
        private fun invalidScreenings() = Stream.of(
            Arguments.arguments(LocalDateTime.now(), TICKET_COUNT, PRICE, InvalidStartAtException::class.java),
            Arguments.arguments(LocalDateTime.now().minusSeconds(1), TICKET_COUNT, PRICE, InvalidStartAtException::class.java),
            Arguments.arguments(START_AT, 0, PRICE, InvalidTicketCountException::class.java),
            Arguments.arguments(START_AT, -1, PRICE, InvalidTicketCountException::class.java),
            Arguments.arguments(START_AT, TICKET_COUNT, Won(999), InvalidPriceException::class.java)
        )
        
        private fun screening(title: String = TITLE, 
                              startAt: LocalDateTime = START_AT,
                              time: PlayTime = TIME,
                              ticketCount: Int = TICKET_COUNT,
                              price: Won = PRICE) = 
            Screening(title, startAt, time, ticketCount, price.value)
    }
}
