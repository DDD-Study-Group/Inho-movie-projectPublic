package com.rein.theater.screening

import com.rein.theater.screening.domain.AlreadyRegisteredScreeningException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice(basePackages = [ "com.rein.theater.screening" ])
class ScreeningControllerExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentExceptionHandler(iae: IllegalArgumentException): ResponseEntity<*> {
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
    
    @ExceptionHandler(AlreadyRegisteredScreeningException::class)
    fun alreadyRegisteredScreeningException(arse: AlreadyRegisteredScreeningException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build<Any>()
    }
}
