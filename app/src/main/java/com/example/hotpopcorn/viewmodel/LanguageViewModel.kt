package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.ViewModel
import java.util.*

abstract class LanguageViewModel : ViewModel() {
    protected val language = if (Locale.getDefault().displayLanguage == "polski") "pl-PL" else "en-US"
}