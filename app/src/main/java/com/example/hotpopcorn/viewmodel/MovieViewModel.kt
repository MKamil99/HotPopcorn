package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotpopcorn.model.api.MovieRepository
import com.example.hotpopcorn.model.api.ApiRequest
import com.example.hotpopcorn.model.Movie
import com.example.hotpopcorn.model.Person
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

// ViewModel which connects Movies' Fragments with Movies' Repository (and API):
class MovieViewModel : ViewModel() {
    private val repository : MovieRepository = MovieRepository(ApiRequest.getAPI())

    //                           MOVIE SEARCH AND POPULAR MOVIES
    var moviesWithMatchingTitle = MutableLiveData<List<Movie>>()
    fun setMoviesWithMatchingTitle(givenText : String) {
        viewModelScope.launch {
            val response = if (givenText != "") repository.searchForMovies(givenText).awaitResponse() else repository.getPopularMovies().awaitResponse()
            if (response.isSuccessful) moviesWithMatchingTitle.value = response.body()?.results?.sortedByDescending { it.popularity }
        }
    }

    //                                      MOVIE DETAILS
    var currentMovie = MutableLiveData<Movie>()
    fun setCurrentMovie(currentMovieID : Int) {
        viewModelScope.launch {
            val response = repository.getMovieDetails(currentMovieID).awaitResponse()
            if (response.isSuccessful) currentMovie.value = response.body()
        }
    }

    //                            PEOPLE CONNECTED WITH THIS MOVIE
    var currentMovieCast = MutableLiveData<List<Person>>()
    var currentMovieCrew = MutableLiveData<List<Person>>()
    fun setPeopleConnectedWithCurrentMovie(currentMovieID : Int) {
        viewModelScope.launch {
            val response = repository.getPeopleFromThisMovie(currentMovieID).awaitResponse()
            if (response.isSuccessful) {
                currentMovieCast.value = response.body()?.cast?.sortedByDescending { it.popularity }
                currentMovieCrew.value = response.body()?.crew?.sortedByDescending { it.popularity }
            }
        }
    }
}