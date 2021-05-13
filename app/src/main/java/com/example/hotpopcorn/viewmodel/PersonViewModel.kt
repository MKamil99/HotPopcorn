package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.LiveData
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
    private val mutPeopleWithMatchingName = MutableLiveData<List<Person>>()
    val peopleWithMatchingName : LiveData<List<Person>> get() = mutPeopleWithMatchingName
    fun setPeopleWithMatchingName(givenText : String) {
        viewModelScope.launch {
            val response =
                if (givenText != "") repository.searchForPeople(givenText).awaitResponse()
                else repository.getPopularPeople().awaitResponse()
            if (response.isSuccessful)
                mutPeopleWithMatchingName.value = response.body()?.results?.sortedByDescending { it.popularity }
        }
    }

    //                                      PERSON DETAILS
    private val mutCurrentPerson = MutableLiveData<Person>()
    val currentPerson : LiveData<Person> get() = mutCurrentPerson
    fun setCurrentPerson(currentPersonID : Int) {
        viewModelScope.launch {
            val response = repository.getPersonDetails(currentPersonID).awaitResponse()
            if (response.isSuccessful) mutCurrentPerson.value = response.body()
        }
    }

    //                         MOVIES AND TV SHOWS CONNECTED WITH THIS PERSON
    private val mutCurrentPersonInCastCollection = MutableLiveData<List<GeneralObject>>()
    private val mutCurrentPersonInCrewCollection = MutableLiveData<List<GeneralObject>>()
    val currentPersonInCastCollection : LiveData<List<GeneralObject>> get() = mutCurrentPersonInCastCollection
    val currentPersonInCrewCollection : LiveData<List<GeneralObject>> get() = mutCurrentPersonInCrewCollection
    fun setCurrentPersonCollection(currentPersonID : Int)
    {
        viewModelScope.launch {
            val response = repository.getMoviesAndTVShowsFromThisPerson(currentPersonID).awaitResponse()
            if (response.isSuccessful) {
                mutCurrentPersonInCastCollection.value = response.body()?.cast?.sortedByDescending { it.popularity }
                mutCurrentPersonInCrewCollection.value = response.body()?.crew?.sortedByDescending { it.popularity }
            }
        }
    }
}