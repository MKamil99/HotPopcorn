package com.example.hotpopcorn.model.api

import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.model.responses.PeopleFromMovieOrTVShowListResponse
import com.example.hotpopcorn.model.responses.TVShowListResponse
import retrofit2.Call

// Functions connected with TVShow objects - general and details:
class TVShowRepository(private val apiRequest : ApiRequest) {
    fun searchForTVShows(someText : String, language : String) : Call<TVShowListResponse> {
        return apiRequest.searchForTVShows(someText, language)
    }
    fun getPopularTVShows(language : String) : Call<TVShowListResponse> {
        return apiRequest.getPopularTVShows(language)
    }
    fun getTVShowDetails(TVShowID : Int, language : String) : Call<TVShow> {
        return apiRequest.getTVShowDetails(TVShowID, language)
    }
    fun getPeopleFromThisTVShow(TVShowID : Int, language : String) : Call<PeopleFromMovieOrTVShowListResponse> {
        return apiRequest.getPeopleFromThisTVShow(TVShowID, language)
    }
}