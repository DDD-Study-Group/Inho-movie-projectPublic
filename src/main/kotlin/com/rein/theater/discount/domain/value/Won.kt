package com.rein.theater.discount.domain.value

class Won(value: Int) {
    val value: Int

    init {
        if (value < MIN) throw IllegalArgumentException()
        this.value = value
    }
    
    operator fun compareTo(other: Won): Int = 
        if (value > other.value) 1
        else if (value < other.value)  -1 
        else 0

    operator fun minus(other: Won): Won = with(value - other.value) {
        if (this < MIN) ZERO
        else Won(this)
    }
    
    operator fun times(percent: Percent): Won = Won((value * percent.rate()).toInt())

    companion object {
        private const val MIN = 0
        private val ZERO = Won(MIN)
    }
}
