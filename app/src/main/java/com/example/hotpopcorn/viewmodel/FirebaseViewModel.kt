package com.example.hotpopcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotpopcorn.model.SavedObject
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch

// ViewModel which stores SavedObjects objects received from Firebase and provides using them in the View:
class FirebaseViewModel : ViewModel() {

    // Current User's Database Reference from Firebase Realtime Database:
    private val mutCurrentUserRef = MutableLiveData<DatabaseReference>()
    val currentUserRef : LiveData<DatabaseReference> get() = mutCurrentUserRef
    fun setCurrentUserRef(ref : DatabaseReference?) {
        if (ref != null) mutCurrentUserRef.value = ref
    }

    // Current User's saved objects:
    private val mutObjectsToWatch = MutableLiveData<List<SavedObject>>()
    private val mutObjectWatched  = MutableLiveData<List<SavedObject>>()
    private val mutObjectsOverall = MutableLiveData<List<SavedObject>>()
    val objectsOverall : LiveData<List<SavedObject>> get() = mutObjectsOverall
    fun setSavedObjectsFromFirebase(savedObjects : List<SavedObject>) {
        viewModelScope.launch {
            mutObjectsToWatch.value = savedObjects
                .filter { savedObject -> savedObject.seen == false }
                .sortedByDescending { x -> x.timeOfSaving }
            mutObjectWatched.value = savedObjects
                .filter { savedObject -> savedObject.seen == true }
                .sortedByDescending { x -> x.timeOfSaving }
            mutObjectsOverall.value = savedObjects
        }
    }

    // Searching in database:
    private val mutMatchingObjectsToWatch      = MutableLiveData<List<SavedObject>>()
    private val mutMatchingObjectsWatched = MutableLiveData<List<SavedObject>>()
    val matchingObjectsToWatch : LiveData<List<SavedObject>> get() = mutMatchingObjectsToWatch
    val matchingObjectsWatched : LiveData<List<SavedObject>> get() = mutMatchingObjectsWatched
    fun setMatchingSavedObjects(givenText : String) {
        viewModelScope.launch {
            // TO WATCH:
            mutMatchingObjectsToWatch.value =
                if (givenText == "") mutObjectsToWatch.value
                else mutObjectsToWatch.value
                    ?.filter { x -> x.title.contains(givenText) }
            // WATCHED:
            mutMatchingObjectsWatched.value =
                if (givenText == "") mutObjectWatched.value
                else mutObjectWatched.value
                    ?.filter { x -> x.title.contains(givenText) }
        }
    }
}