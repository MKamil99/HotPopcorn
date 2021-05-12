package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.model.api.ApiRequest
import com.example.hotpopcorn.model.api.TVShowRepository
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

// ViewModel which connects TV Shows' Fragments with TV Shows' Repository (and API):
class TVShowViewModel : ViewModel() {
    private val repository : TVShowRepository = TVShowRepository(ApiRequest.getAPI())

    //                                TV SHOW SEARCH AND POPULAR TV SHOWS
    var TVShowsWithMatchingTitle = MutableLiveData<List<TVShow>>()
    fun setTVShowsWithMatchingTitle(givenText : String) {
        viewModelScope.launch {
            val response =
                if (givenText != "") repository.searchForTVShows(givenText).awaitResponse()
                else repository.getPopularTVShows().awaitResponse()
            if (response.isSuccessful)
                TVShowsWithMatchingTitle.value = response.body()?.results?.sortedByDescending { it.popularity }
        }
    }

    //                                      TV SHOW DETAILS
    var currentTVShow = MutableLiveData<TVShow>()
    fun setCurrentTVShow(currentTVShowID : Int) {
        viewModelScope.launch {
            val response = repository.getTVShowDetails(currentTVShowID).awaitResponse()
            if (response.isSuccessful) currentTVShow.value = response.body()
        }
    }

    //                            PEOPLE CONNECTED WITH THIS TV SHOW
    var currentTVShowCast = MutableLiveData<List<Person>>()
    var currentTVShowCrew = MutableLiveData<List<Person>>()
    fun setPeopleConnectedWithCurrentTVShow(currentTVShowID : Int) {
        viewModelScope.launch {
            val response = repository.getPeopleFromThisTVShow(currentTVShowID).awaitResponse()
            if (response.isSuccessful) {
                currentTVShowCast.value = response.body()?.cast?.sortedByDescending { it.popularity }
                currentTVShowCrew.value = response.body()?.crew?.sortedByDescending { it.popularity }
            }
        }
    }
}