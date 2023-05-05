package com.rein.theater.movie.view

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.rein.theater.movie.domain.PlayTime

data class SearchMoviesResponse @JsonCreator constructor(@field:JsonProperty("movies") val movies: Set<SearchMovieResponse>)

data class SearchMovieResponse @JsonCreator constructor(
    @field:JsonProperty("title") val title: String,
    @field:JsonProperty("playTime") val playTime: PlayTime
)
