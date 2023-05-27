package com.rein.theater.discount.application

import com.rein.theater.discount.domain.*
import com.rein.theater.discount.domain.value.Percent
import com.rein.theater.screening.domain.AlreadyRegisteredScreeningException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ExtendWith(MockKExtension::class)
class CreateDiscountServiceTest {
    @RelaxedMockK
    private lateinit var repository: DiscountRepository

    @InjectMockKs
    private lateinit var cut: CreateDiscountService

    @DisplayName("이미 등록된 할인이라면 AlreadyRegisteredScreeningException 예외를 발생시킨다.")
    @Test
    fun create_when_already_registered_discount() {
        every { repository.save(any()) } throws AlreadyRegisteredScreeningException(DISCOUNT)

        assertThatThrownBy { cut.create(DISCOUNT) }.isExactlyInstanceOf(AlreadyRegisteredScreeningException::class.java)
    }

    @DisplayName("할인을 저장하는 중 오류가 발생하면 FailedToCreateDiscountException 예외를 발생시킨다.")
    @Test
    fun create_when_occured_unknown_exception() {
        val unknown = IOException()
        every { repository.save(any()) } throws unknown

        with(DISCOUNT) {
            assertThatThrownBy { cut.create(this) }.isExactlyInstanceOf(FailedToCreateDiscountException::class.java)
                .hasMessage("Failed to create discount. condition(date=${condition.date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))}, order=${condition.order}), policy(id=${policy.id()}, value=${policy.value()})")
        }
    }

    companion object {
        private val DISCOUNT = discount()
        
        private fun discount(condition: Condition = condition()): Discount =
            Discount(condition, PercentPolicy(Percent(10)))
        
        private fun condition(date: LocalDate = LocalDate.now().plusDays(10), order: Int = 3): Condition = Condition(date, order)
    }
}
