package com.example.hotpopcorn.model.responses

import com.example.hotpopcorn.model.Person

// Used in getting list of popular people and people with matching name:
class PersonListResponse(val results : List<Person>)