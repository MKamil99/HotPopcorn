package com.example.hotpopcorn.model.responses

import com.example.hotpopcorn.model.TVShow

// Used in getting list of popular tv shows and tv shows with matching title:
data class TVShowListResponse(val results : List<TVShow>)