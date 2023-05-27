package com.rein.theater.discount.domain

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.util.stream.Stream

@DisplayName("할인 조건 테스트")
class ConditionTest {
    @DisplayName("할인 날짜가 현재보다 24시간 이후가 아니거나 순서가 1보다 작다면 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("invalidConditionSet")
    fun create_when_invalid_arguments(date: LocalDate, order: Int, expected: Class<InvalidDiscountArgumentException>) {
        assertThatThrownBy { Condition(date, order) }.isExactlyInstanceOf(expected)
    }
    
    companion object {
        @JvmStatic
        private fun invalidConditionSet() = Stream.of(
            Arguments.arguments(LocalDate.now(), 1, InvalidDiscountDateException::class.java),
            Arguments.arguments(LocalDate.now().minusDays(1), 1, InvalidDiscountDateException::class.java),
            Arguments.arguments(LocalDate.now().plusDays(1), 0, InvalidDiscountOrderException::class.java),
            Arguments.arguments(LocalDate.now().plusDays(1), -1, InvalidDiscountOrderException::class.java)
        )
    }
}
