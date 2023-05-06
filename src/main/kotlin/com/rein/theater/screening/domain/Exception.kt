package com.rein.theater.screening.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlreadyRegisteredScreeningException(dateTime: LocalDateTime) : RuntimeException(
    "Already a screening registered. dateTime=${
        dateTime.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        )
    }"
)
