package com.example.hotpopcorn.model.api

import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.model.responses.MoviesAndTVShowsFromPersonListResponse
import com.example.hotpopcorn.model.responses.PersonListResponse
import retrofit2.Call

// Functions connected with Person objects - general and details:
class PersonRepository(private val apiRequest : ApiRequest) {
    fun searchForPeople(someText : String) : Call<PersonListResponse> = apiRequest.searchForPeople(someText)
    fun getPopularPeople() : Call<PersonListResponse> = apiRequest.getPopularPeople()
    fun getPersonDetails(personID : Int) : Call<Person> = apiRequest.getPersonDetails(personID)
    fun getMoviesAndTVShowsFromThisPerson(personID : Int) : Call<MoviesAndTVShowsFromPersonListResponse> = apiRequest.getMoviesAndTVShowsFromThisPerson(personID)
}