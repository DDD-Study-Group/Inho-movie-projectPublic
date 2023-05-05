package com.rein.theater.movie.domain

class InvalidMovieException(movie: Movie) : RuntimeException("The movie information is not valid. movie=$movie")

class AlreadyRegisteredMovieException : RuntimeException("The movie is already registered.")
