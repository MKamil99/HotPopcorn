package com.example.hotpopcorn.model

data class SavedObject(
    val media_type: String? = null,    // tv or movie
    val movieOrTVShowID: Int? = null,
    val title: String? = null,
    val poster_path: String? = null,
    val release_date: String? = null,
    val date: String? = null,
    val seen: Boolean? = null
)