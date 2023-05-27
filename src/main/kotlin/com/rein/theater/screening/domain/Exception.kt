package com.rein.theater.screening.domain

import com.rein.theater.discount.domain.Discount
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AlreadyRegisteredScreeningException : RuntimeException {
    constructor(dateTime: LocalDateTime) : super(
        "Already a screening registered. dateTime=${
            dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        }"
    )
    
    constructor(id: ID) : super("Already a screening registered. id=$id")
    
    constructor(discount: Discount) : super("Already a screening registered. id=${discount.id()}, date=${discount.date()}, order=${discount.order()}")
}
    

abstract class InvalidScreeningArgumentException(message: String) : IllegalArgumentException(message)

class InvalidStartAtException(startAt: LocalDateTime) : InvalidScreeningArgumentException("Invalid startAt. startAt=$startAt")

class InvalidEndAtException(startAt: LocalDateTime, endAt: LocalDateTime) : InvalidScreeningArgumentException("Invalid endAt. startAt=$startAt, endAt=$endAt")

class InvalidTitleException(title: String) : InvalidScreeningArgumentException("Invalid movie title. title=$title")

class InvalidPriceException(price: Int) : InvalidScreeningArgumentException("Invalid reserve price. price=$price")

class InvalidTicketCountException(ticketCount: Int) : InvalidScreeningArgumentException("Invalid ticket count. ticketCount=$ticketCount")
