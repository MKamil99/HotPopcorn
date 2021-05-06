package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotpopcorn.model.GeneralObject
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.model.api.ApiRequest
import com.example.hotpopcorn.model.api.PersonRepository
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

// ViewModel which connects People's Fragments with People's Repository (and API):
class PersonViewModel : ViewModel() {
    private val repository : PersonRepository = PersonRepository(ApiRequest.getAPI())

    //                             PERSON SEARCH AND POPULAR PEOPLE
    var peopleWithMatchingName = MutableLiveData<List<Person>>()
    fun setPeopleWithMatchingName(givenText : String)
    {
        viewModelScope.launch {
            val response = if (givenText != "") repository.searchForPeople(givenText).awaitResponse() else repository.getPopularPeople().awaitResponse()
            if (response.isSuccessful) peopleWithMatchingName.value = response.body()?.results?.sortedByDescending { it.popularity }
        }
    }

    //                                      PERSON DETAILS
    var currentPerson = MutableLiveData<Person>()
    fun setCurrentPerson(currentPersonID : Int) {
        viewModelScope.launch {
            val response = repository.getPersonDetails(currentPersonID).awaitResponse()
            if (response.isSuccessful) currentPerson.value = response.body()
        }
    }

    //                         MOVIES AND TV SHOWS CONNECTED WITH THIS PERSON
    var currentPersonInCastCollection = MutableLiveData<List<GeneralObject>>()
    var currentPersonInCrewCollection = MutableLiveData<List<GeneralObject>>()
    fun setCurrentPersonCollection(currentPersonID : Int)
    {
        viewModelScope.launch {
            val response = repository.getMoviesAndTVShowsFromThisPerson(currentPersonID).awaitResponse()
            if (response.isSuccessful) {
                currentPersonInCastCollection.value = response.body()?.cast?.sortedByDescending { it.popularity }
                currentPersonInCrewCollection.value = response.body()?.crew?.sortedByDescending { it.popularity }
            }
        }
    }
}