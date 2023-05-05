package com.rein.theater.movie

import com.rein.theater.movie.domain.AlreadyRegisteredMovieException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice(basePackages = [ "com.rein.theater.movie" ])
class MovieControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(manv: MethodArgumentNotValidException): ResponseEntity<*> {
        return ResponseEntity.badRequest().build<Any>()
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(cve: ConstraintViolationException): ResponseEntity<*> {
        return ResponseEntity.badRequest().build<Any>()
    }

    @ExceptionHandler(AlreadyRegisteredMovieException::class)
    fun alreadyRegisteredMovieException(arme: AlreadyRegisteredMovieException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build<Any>()
    }
}
