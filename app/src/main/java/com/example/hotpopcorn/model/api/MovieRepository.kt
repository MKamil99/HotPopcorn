package com.example.hotpopcorn.model.api

import com.example.hotpopcorn.model.*
import com.example.hotpopcorn.model.responses.MovieListResponse
import com.example.hotpopcorn.model.responses.PeopleFromMovieOrTVShowListResponse
import retrofit2.Call

// Functions connected with Movie objects - general and details:
class MovieRepository(private val apiRequest : ApiRequest) {
    fun searchForMovies(someText : String, language : String) : Call<MovieListResponse> {
        return apiRequest.searchForMovies(someText, language)
    }
    fun getPopularMovies(language : String) : Call<MovieListResponse> {
        return apiRequest.getPopularMovies(language)
    }
    fun getMovieDetails(movieID : Int, language : String) : Call<Movie> {
        return apiRequest.getMovieDetails(movieID, language)
    }
    fun getPeopleFromThisMovie(movieID : Int, language : String) : Call<PeopleFromMovieOrTVShowListResponse> {
        return apiRequest.getPeopleFromThisMovie(movieID, language)
    }
}