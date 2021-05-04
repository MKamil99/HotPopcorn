package com.example.hotpopcorn.model.api

import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.model.responses.PeopleFromMovieOrTVShowListResponse
import com.example.hotpopcorn.model.responses.TVShowListResponse
import retrofit2.Call

// Functions connected with TVShow objects - general and details:
class TVShowRepository(private val apiRequest : ApiRequest) {
    fun searchForTVShows(someText : String) : Call<TVShowListResponse> = apiRequest.searchForTVShows(someText)
    fun getPopularTVShows() : Call<TVShowListResponse> = apiRequest.getPopularTVShows()
    fun getTVShowDetails(TVShowID : Int) : Call<TVShow> = apiRequest.getTVShowDetails(TVShowID)
    fun getPeopleFromThisTVShow(TVShowID: Int) : Call<PeopleFromMovieOrTVShowListResponse> = apiRequest.getPeopleFromThisTVShow(TVShowID)
}