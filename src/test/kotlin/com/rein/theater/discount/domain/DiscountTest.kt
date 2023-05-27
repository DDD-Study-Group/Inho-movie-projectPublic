package com.rein.theater.discount.domain

import com.rein.theater.discount.domain.value.Percent
import com.rein.theater.discount.domain.value.Won
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Stream

@DisplayName("할인 Domain 테스트")
class DiscountTest {
    @DisplayName("할인 적용대상이면 할인금액을 제외한 금액을 지불하고, 할인 적용대상이 아니라면 할인금액을 제외하지 않는다.")
    @ParameterizedTest
    @MethodSource("paidSet")
    fun paidAmount(discount: Discount, date: LocalDate, order: Int, price: Won, expected: Won) {
        assertThat(discount.paidAmount(date, order, price)).isEqualTo(expected)
    }

    @DisplayName("할인조건과 할인정책이 유효하다면 할인을 등록할 수 있다.")
    @ParameterizedTest
    @MethodSource("registSet")
    fun regist(condition: Condition, policy: Policy) {
        val discount = Discount(condition, policy)
        
        assertThat(discount.regist()).isEqualTo(discount)
    }

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

    @DisplayName("할인 날짜가 현재보다 2일 이후가 아니거나 순서가 1보다 작다면 할인을 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidConditionSet")
    fun regist_when_invalid_discount_condition(date: LocalDate, order: Int, expected: Class<InvalidDiscountArgumentException>) {
        assertThatThrownBy { Discount(Condition(date, order), PercentPolicy(Percent(10))).regist() }.isExactlyInstanceOf(expected)
    }

    @DisplayName("할인금액이 1000원 미만이거나, 할인비율이 1% 미만이면 할인을 등록할 수 없다.")
    @ParameterizedTest
    @MethodSource("invalidPolicySet")
    fun regist_when_invalid_discount_policy(condition: Condition, policy: Policy, expected: Class<InvalidDiscountArgumentException>) {
        assertThatThrownBy { Discount(condition, policy).regist() }.isExactlyInstanceOf(expected)
    }

    companion object {
        private val DATE = LocalDate.now().plusDays(3)
        private val FORMAT = DateTimeFormatter.BASIC_ISO_DATE
        
        @JvmStatic
        private fun paidSet() = Stream.of(
            Arguments.arguments(
                Discount(Condition(DATE, 3), AmountPolicy(Won(3000))),
                DATE, 3, Won(10000),
                Won(7000)
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 3), AmountPolicy(Won(3000))),
                DATE.plusDays(1), 3, Won(10000),
                Won(10000)
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 3), AmountPolicy(Won(3000))),
                DATE, 2, Won(10000),
                Won(10000)
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 1), PercentPolicy(Percent(20))),
                DATE, 1, Won(10000),
                Won(8000)
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 1), PercentPolicy(Percent(20))),
                DATE, 2, Won(10000),
                Won(10000)
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 1), PercentPolicy(Percent(20))),
                DATE.minusDays(1), 1, Won(10000),
                Won(10000)
            )
        )
        
        @JvmStatic 
        private fun idSet() = Stream.of(
            Arguments.arguments(Condition(DATE, 3), PercentPolicy(Percent(10)), Objects.hash(DATE.format(FORMAT), 3)),
            Arguments.arguments(Condition(DATE.plusDays(1), 2), AmountPolicy(Won(2000)), Objects.hash(DATE.plusDays(1).format(FORMAT), 2)),
            Arguments.arguments(Condition(DATE, 1), PercentPolicy(Percent(5)), Objects.hash(DATE.format(FORMAT), 1))
        )
        
        @JvmStatic
        private fun equalsSet() = Stream.of(
            Arguments.arguments(
                Discount(Condition(DATE, 3), PercentPolicy(Percent(10))),
                Discount(Condition(DATE, 3), AmountPolicy(Won(10))),
                true
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 3), PercentPolicy(Percent(10))),
                Discount(Condition(DATE, 2), PercentPolicy(Percent(10))),
                false
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 3), PercentPolicy(Percent(10))),
                Discount(Condition(DATE.plusDays(1), 3), PercentPolicy(Percent(10))),
                false
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 3), AmountPolicy(Won(1000))),
                Discount(Condition(DATE, 2), AmountPolicy(Won(1000))),
                false
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 3), AmountPolicy(Won(1000))),
                Discount(Condition(DATE.plusDays(1), 3), AmountPolicy(Won(1000))),
                false
            ),
            Arguments.arguments(
                Discount(Condition(DATE, 3), AmountPolicy(Won(1000))),
                Discount(Condition(DATE, 3), AmountPolicy(Won(1000))),
                true
            )
        )

        @JvmStatic
        private fun invalidConditionSet() = Stream.of(
            Arguments.arguments(LocalDate.now(), 1, InvalidDiscountDateException::class.java),
            Arguments.arguments(LocalDate.now().minusDays(1), 1, InvalidDiscountDateException::class.java),
            Arguments.arguments(LocalDate.now().plusDays(2), 0, InvalidDiscountOrderException::class.java),
            Arguments.arguments(LocalDate.now().plusDays(2), -1, InvalidDiscountOrderException::class.java)
        )
        
        @JvmStatic
        private fun invalidPolicySet() = Stream.of(
            Arguments.arguments(Condition(DATE, 3), AmountPolicy(Won(999)), InvalidDiscountAmountException::class.java),
            Arguments.arguments(Condition(DATE, 1), AmountPolicy(Won(0)), InvalidDiscountAmountException::class.java),
            Arguments.arguments(Condition(DATE, 1), PercentPolicy(0), InvalidDiscountPercentException::class.java)
        )
        
        @JvmStatic
        private fun registSet() = Stream.of(
            Arguments.arguments(Condition(DATE, 3), PercentPolicy(Percent(10))),
            Arguments.arguments(Condition(DATE.plusMonths(1), 2), AmountPolicy(Won(1000)))
        )
    }
}
