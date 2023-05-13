package com.rein.theater.screening.domain

import com.rein.theater.movie.domain.PlayTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*


@Repository
class ScreeningRepository {
    fun save(screening: Screening): Screening {
        if (findById(screening.id).isPresent) throw AlreadyRegisteredScreeningException(screening.id)
        return Screening("title", LocalDateTime.now(), PlayTime.minutes(120), 100, 10000)
    }
    
    private fun findById(id: ID): Optional<Screening> {
        return Optional.of(Screening("title", LocalDateTime.now(), PlayTime.minutes(120), 100, 10000))
    }
}
