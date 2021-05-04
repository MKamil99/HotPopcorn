package com.example.hotpopcorn.model.responses

import com.example.hotpopcorn.model.Movie

// Used in getting list of popular movies and movies with matching title:
data class MovieListResponse(val results : List<Movie>)