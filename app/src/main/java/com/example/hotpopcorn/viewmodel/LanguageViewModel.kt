package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.ViewModel

abstract class LanguageViewModel : ViewModel() {
    // The TMDB API provides using many different languages,
    // but the translations are often in bad condition, so for now
    // the best option is to make the "language" variable constant
    // (with possibility to change this VM's code in the future).
    protected val language = "en-US"
}