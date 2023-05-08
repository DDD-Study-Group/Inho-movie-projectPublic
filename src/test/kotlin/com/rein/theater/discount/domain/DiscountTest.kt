package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Percent
import com.rein.theater.discount.domain.value.Won
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.autoconfigure.web.ServerProperties.Jetty.Accesslog.FORMAT
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Objects
import java.util.stream.Stream

class DiscountTest {
    @DisplayName("할인의 ID 는 할인날짜와 순서의 hash 값이다.")
    @ParameterizedTest
    @MethodSource("idSet")
    fun id(condition: Condition, policy: Policy, expected: Int) {
        assertThat(Discount(condition, policy).id()).isEqualTo(expected)
    }

    @DisplayName("ID 가 같다면 동일한 할인이고, ID 가 다르다면 서로 다른 할인이다.")
    @ParameterizedTest
    @MethodSource("equalsSet")
    fun testEquals(discount: Discount, other: Discount, expected: Boolean) {
        assertThat(discount == other).isEqualTo(expected)
    }
    
    companion object {
        private val NOW = LocalDate.now()
        private val FORMAT = DateTimeFormatter.BASIC_ISO_DATE
        
        @JvmStatic 
        private fun idSet() = Stream.of(
            Arguments.arguments(Condition(NOW, 3), PercentPolicy(Percent(10)), Objects.hash(NOW.format(FORMAT), 3)),
            Arguments.arguments(Condition(NOW.plusDays(1), 2), AmountPolicy(Won(2000)), Objects.hash(NOW.plusDays(1).format(FORMAT), 2)),
            Arguments.arguments(Condition(NOW, 1), PercentPolicy(Percent(5)), Objects.hash(NOW.format(FORMAT), 1))
        )
        
        @JvmStatic
        private fun equalsSet() = Stream.of(
            Arguments.arguments(
                Discount(Condition(NOW, 3), PercentPolicy(Percent(10))),
                Discount(Condition(NOW, 3), AmountPolicy(Won(10))),
                true
            ),
            Arguments.arguments(
                Discount(Condition(NOW, 3), PercentPolicy(Percent(10))),
                Discount(Condition(NOW, 2), PercentPolicy(Percent(10))),
                false
            ),
            Arguments.arguments(
                Discount(Condition(NOW, 3), PercentPolicy(Percent(10))),
                Discount(Condition(NOW.plusDays(1), 3), PercentPolicy(Percent(10))),
                false
            ),
            Arguments.arguments(
                Discount(Condition(NOW, 3), AmountPolicy(Won(1000))),
                Discount(Condition(NOW, 2), AmountPolicy(Won(1000))),
                false
            ),
            Arguments.arguments(
                Discount(Condition(NOW, 3), AmountPolicy(Won(1000))),
                Discount(Condition(NOW.plusDays(1), 3), AmountPolicy(Won(1000))),
                false
            ),
            Arguments.arguments(
                Discount(Condition(NOW, 3), AmountPolicy(Won(1000))),
                Discount(Condition(NOW, 3), AmountPolicy(Won(1000))),
                true
            )
        )
    }
}
