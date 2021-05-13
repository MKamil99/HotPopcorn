package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.LiveData
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
    private val mutMoviesWithMatchingTitle = MutableLiveData<List<Movie>>()
    val moviesWithMatchingTitle : LiveData<List<Movie>> get() = mutMoviesWithMatchingTitle
    fun setMoviesWithMatchingTitle(givenText : String) {
        viewModelScope.launch {
            val response =
                if (givenText != "") repository.searchForMovies(givenText).awaitResponse()
                else repository.getPopularMovies().awaitResponse()
            if (response.isSuccessful)
                mutMoviesWithMatchingTitle.value = response.body()?.results?.sortedByDescending { it.popularity }
        }
    }

    //                                      MOVIE DETAILS
    private val mutCurrentMovie = MutableLiveData<Movie>()
    val currentMovie : LiveData<Movie> get() = mutCurrentMovie
    fun setCurrentMovie(currentMovieID : Int) {
        viewModelScope.launch {
            val response = repository.getMovieDetails(currentMovieID).awaitResponse()
            if (response.isSuccessful) mutCurrentMovie.value = response.body()
        }
    }

    //                            PEOPLE CONNECTED WITH THIS MOVIE
    private val mutCurrentMovieCast = MutableLiveData<List<Person>>()
    private val mutCurrentMovieCrew = MutableLiveData<List<Person>>()
    val currentMovieCast : LiveData<List<Person>> get() = mutCurrentMovieCast
    val currentMovieCrew : LiveData<List<Person>> get() = mutCurrentMovieCrew
    fun setPeopleConnectedWithCurrentMovie(currentMovieID : Int) {
        viewModelScope.launch {
            val response = repository.getPeopleFromThisMovie(currentMovieID).awaitResponse()
            if (response.isSuccessful) {
                mutCurrentMovieCast.value = response.body()?.cast?.sortedByDescending { it.popularity }
                mutCurrentMovieCrew.value = response.body()?.crew?.sortedByDescending { it.popularity }
            }
        }
    }
}