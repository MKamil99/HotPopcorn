package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.model.api.ApiRequest
import com.example.hotpopcorn.model.api.TVShowRepository
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

// ViewModel which connects TV Shows' Fragments with TV Shows' Repository (and API):
class TVShowViewModel : LanguageViewModel() {
    private val repository : TVShowRepository = TVShowRepository(ApiRequest.getAPI())

    //                                TV SHOW SEARCH AND POPULAR TV SHOWS
    private val mutTVShowsWithMatchingTitle = MutableLiveData<List<TVShow>>()
    val TVShowsWithMatchingTitle : LiveData<List<TVShow>> get() = mutTVShowsWithMatchingTitle
    fun setTVShowsWithMatchingTitle(givenText : String) {
        viewModelScope.launch {
            val response =
                if (givenText != "") repository.searchForTVShows(givenText, language).awaitResponse()
                else repository.getPopularTVShows(language).awaitResponse()
            if (response.isSuccessful)
                mutTVShowsWithMatchingTitle.value = response.body()?.results?.sortedByDescending { it.popularity }
        }
    }

    //                                      TV SHOW DETAILS
    private val mutCurrentTVShow = MutableLiveData<TVShow>()
    val currentTVShow : LiveData<TVShow> get() = mutCurrentTVShow
    fun setCurrentTVShow(currentTVShowID : Int) {
        viewModelScope.launch {
            val response = repository.getTVShowDetails(currentTVShowID, language).awaitResponse()
            if (response.isSuccessful) mutCurrentTVShow.value = response.body()
        }
    }

    //                            PEOPLE CONNECTED WITH THIS TV SHOW
    private val mutCurrentTVShowCast = MutableLiveData<List<Person>>()
    private val mutCurrentTVShowCrew = MutableLiveData<List<Person>>()
    val currentTVShowCast : LiveData<List<Person>> get() = mutCurrentTVShowCast
    val currentTVShowCrew : LiveData<List<Person>> get() = mutCurrentTVShowCrew
    fun setPeopleConnectedWithCurrentTVShow(currentTVShowID : Int) {
        viewModelScope.launch {
            val response = repository.getPeopleFromThisTVShow(currentTVShowID, language).awaitResponse()
            if (response.isSuccessful) {
                mutCurrentTVShowCast.value = response.body()?.cast?.sortedByDescending { it.popularity }
                mutCurrentTVShowCrew.value = response.body()?.crew?.sortedByDescending { it.popularity }
            }
        }
    }
}