package com.rein.theater.discount.domain.value

class Percent constructor(value: Int) {
    val value: Int

    init {
        if (value < MIN || value > MAX) throw IllegalArgumentException()
        this.value = value
    }
    
    fun rate(): Float = value.toFloat() / 100

    operator fun compareTo(other: Percent): Int =
        if (value > other.value) 1
        else if (value < other.value)  -1
        else 0
    
    companion object {
        private const val MIN = 0
        private const val MAX = 100
    }
}
