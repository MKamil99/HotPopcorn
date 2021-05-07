package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotpopcorn.model.SavedObject
import com.google.firebase.database.DatabaseReference

class FirebaseViewModel : ViewModel() {

    // Current User's Database Reference from Firebase Realtime Database:
    var currentUserRef = MutableLiveData<DatabaseReference>()
    fun setCurrentUserRef(ref : DatabaseReference?) {
        if (ref != null) currentUserRef.value = ref
    }

    // Current User's saved objects:
    var moviesAndShowsThatAreToWatch      = MutableLiveData<List<SavedObject>>()
    var moviesAndShowsThatHaveBeenWatched = MutableLiveData<List<SavedObject>>()
    var moviesAndShowsOverall = MutableLiveData<List<SavedObject>>()
    fun setSavedObjectFromFirebase(savedObjects : List<SavedObject>) {
        moviesAndShowsThatAreToWatch.value = savedObjects.filter { savedObject -> savedObject.seen == false }
        moviesAndShowsThatHaveBeenWatched.value = savedObjects.filter { savedObject -> savedObject.seen == true }
        moviesAndShowsOverall.value = savedObjects
    }
}