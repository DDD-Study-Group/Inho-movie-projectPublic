package com.rein.theater.discount

import com.rein.theater.discount.domain.AlreadyCreatedDiscountException
import com.rein.theater.discount.domain.InvalidDiscountDateException
import com.rein.theater.discount.view.InvalidDiscountDateResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice(basePackages = [ "com.rein.theater.discount" ])
class DiscountControllerExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentExceptionHandler(e: IllegalArgumentException): ResponseEntity<*> {
        return ResponseEntity.badRequest().build<Any>()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(manv: MethodArgumentNotValidException): ResponseEntity<*> {
        return ResponseEntity.badRequest().build<Any>()
    }
    
    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(cve: ConstraintViolationException): ResponseEntity<*> {
        return ResponseEntity.badRequest().build<Any>()
    }

    @ExceptionHandler(AlreadyCreatedDiscountException::class)
    fun alreadyCreatedDiscountException(acde: AlreadyCreatedDiscountException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build<Any>()
    }

    @ExceptionHandler(InvalidDiscountDateException::class)
    fun invalidDiscountDateException(idd: InvalidDiscountDateException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(InvalidDiscountDateResponse(idd.date))
    }
}
