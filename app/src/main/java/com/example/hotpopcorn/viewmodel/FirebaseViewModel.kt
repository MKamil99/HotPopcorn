package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotpopcorn.model.SavedObject
import com.google.firebase.database.DatabaseReference

// ViewModel which stores SavedObjects objects received from Firebase and provides using them in the View:
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
    fun setSavedObjectsFromFirebase(savedObjects : List<SavedObject>) {
        moviesAndShowsThatAreToWatch.value = savedObjects.filter { savedObject -> savedObject.seen == false }
        moviesAndShowsThatHaveBeenWatched.value = savedObjects.filter { savedObject -> savedObject.seen == true }
        moviesAndShowsOverall.value = savedObjects
    }

    // Searching in database:
    var matchingMoviesAndShowsThatAreToWatch = MutableLiveData<List<SavedObject>>()
    var matchingMoviesAndShowsThatHaveBeenWatched = MutableLiveData<List<SavedObject>>()
    fun setMatchingSavedObjects(givenText : String) {
        // TO WATCH:
        matchingMoviesAndShowsThatAreToWatch.value =
            if (givenText == "") { moviesAndShowsThatAreToWatch.value }
            else { moviesAndShowsThatAreToWatch.value?.filter {
                    x -> x.title != null && x.title.contains(givenText) } }
        // WATCHED:
        matchingMoviesAndShowsThatHaveBeenWatched.value =
            if (givenText == "") { moviesAndShowsThatHaveBeenWatched.value }
            else { moviesAndShowsThatHaveBeenWatched.value?.filter {
                    x -> x.title != null && x.title.contains(givenText) } }
    }
}