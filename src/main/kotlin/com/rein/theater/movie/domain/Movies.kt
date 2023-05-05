package com.rein.theater.movie.domain

data class Movies(private val movies: Set<Movie> = emptySet()) : Iterable<Movie> {
    override fun iterator(): Iterator<Movie> = movies.iterator()
    fun exist(): Boolean = movies.isNotEmpty()
}
