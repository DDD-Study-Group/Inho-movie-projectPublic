package com.rein.theater.screening.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
abstract class ScreeningRepository {
    abstract fun save(screening: Screening): Screening
}
