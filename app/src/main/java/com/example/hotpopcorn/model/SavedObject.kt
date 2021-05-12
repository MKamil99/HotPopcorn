package com.example.hotpopcorn.model

data class SavedObject(
    val media_type: String? = null,    // tv or movie
    val movieOrTVShowID: Int? = null,
    val title: String = "",
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val timeOfSaving: Long? = null,
    val seen: Boolean? = null
)