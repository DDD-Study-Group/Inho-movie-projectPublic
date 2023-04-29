package com.rein.theater.discount.domain.value

class Percent constructor(value: Int) {
    val value: Int
    
    fun rate(): Float = value.toFloat() / 100

    init {
        if (value <= MIN || value > MAX) throw IllegalArgumentException()
        this.value = value
    }
    
    companion object {
        private const val MIN = 0
        private const val MAX = 100
    }
}
