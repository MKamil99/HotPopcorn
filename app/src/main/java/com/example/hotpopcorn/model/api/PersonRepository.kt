package com.example.hotpopcorn.model.api

import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.model.responses.MoviesAndTVShowsFromPersonListResponse
import com.example.hotpopcorn.model.responses.PersonListResponse
import retrofit2.Call

// Functions connected with Person objects - general and details:
class PersonRepository(private val apiRequest : ApiRequest) {
    fun searchForPeople(someText : String, language : String) : Call<PersonListResponse> {
        return apiRequest.searchForPeople(someText, language)
    }
    fun getPopularPeople(language : String) : Call<PersonListResponse> {
        return apiRequest.getPopularPeople(language)
    }
    fun getPersonDetails(personID : Int, language : String) : Call<Person> {
        return apiRequest.getPersonDetails(personID, language)
    }
    fun getMoviesAndTVShowsFromThisPerson(personID : Int, language : String) : Call<MoviesAndTVShowsFromPersonListResponse> {
        return apiRequest.getMoviesAndTVShowsFromThisPerson(personID, language)
    }
}