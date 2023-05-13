package com.rein.theater.screening.domain

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AlreadyRegisteredScreeningException(dateTime: LocalDateTime) : RuntimeException(
    "Already a screening registered. dateTime=${
        dateTime.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        )
    }"
)

abstract class InvalidScreeningArgumentException(message: String) : IllegalArgumentException(message)

class InvalidStartAtException(startAt: LocalDateTime) : InvalidScreeningArgumentException("Invalid startAt. startAt=$startAt")

class InvalidEndAtException(startAt: LocalTime, endAt: LocalTime) : InvalidScreeningArgumentException("Invalid endAt. startAt=$startAt, endAt=$endAt")

class InvalidTitleException(title: String) : InvalidScreeningArgumentException("Invalid movie title. title=$title")

class InvalidPriceException(price: Int) : InvalidScreeningArgumentException("Invalid reserve price. price=$price")

class InvalidTicketCountException(ticketCount: Int) : InvalidScreeningArgumentException("Invalid ticket count. ticketCount=$ticketCount")
