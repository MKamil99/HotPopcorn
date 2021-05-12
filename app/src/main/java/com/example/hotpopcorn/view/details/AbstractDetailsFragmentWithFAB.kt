package com.example.hotpopcorn.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.example.hotpopcorn.R
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.viewmodel.FirebaseViewModel
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

// Class which adds functionalities for FAB in all Details Fragments that inherit from it:
abstract class AbstractDetailsFragmentWithFAB : AbstractDetailsFragment() {
    private lateinit var firebaseVM : FirebaseViewModel

    // Binding fragment with Firebase ViewModel:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseVM = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)
    }

    // Displaying FAB and managing Firebase database:
    @SuppressLint("SimpleDateFormat")
    protected fun displayFAB(fab : FloatingActionButton,
                             currentObjectID : Int,
                             currentObjectTitle : String,
                             currentObjectPosterPath : String?,
                             currentObjectReleaseDate : String?,
                             currentObjectMediaType : String) {
        fab.visibility = View.VISIBLE
        changeFABColor(fab, R.color.gray)
        firebaseVM.moviesAndShowsOverall.observe(viewLifecycleOwner, {
            // Checking database:
            val savedObject = firebaseVM.moviesAndShowsOverall.value?.find { savedObject ->
                savedObject.movieOrTVShowID == currentObjectID && savedObject.media_type == currentObjectMediaType }
            val objectToSave = SavedObject(currentObjectMediaType, currentObjectID, currentObjectTitle,
                currentObjectPosterPath, currentObjectReleaseDate,
                SimpleDateFormat("dd-MM-yyyy").format(Date()),
                savedObject != null) // if is in the lists, move from 'To Watch' to 'Watched' (seen = true)
            val rowInDatabase = firebaseVM.currentUserRef.value?.child(
                "$currentObjectMediaType-$currentObjectID")

            // Case 1: Movie / TV Show is not in "To Watch" and not in "Watched":
            if (savedObject == null) {
                changeFABColor(fab, R.color.gray)
                fab.setOnClickListener {
                    rowInDatabase?.setValue(objectToSave)?.addOnCompleteListener {
                        showToast(it, getString(R.string.added_to_towatch))
                    }
                }
            } else {
                // Case 2: Movie / TV Show is in "To Watch":
                if (savedObject.seen == false) {
                    changeFABColor(fab, android.R.color.holo_red_dark)
                    fab.setOnClickListener {
                        rowInDatabase?.setValue(objectToSave)?.addOnCompleteListener {
                            showToast(it, getString(R.string.added_to_watched))
                        }
                    }
                // Case 3: Movie / TV Show is in "Watched":
                } else {
                    changeFABColor(fab, android.R.color.holo_green_dark)
                    fab.setOnClickListener {
                        rowInDatabase?.removeValue()?.addOnCompleteListener {
                            showToast(it, getString(R.string.added_to_none))
                        }
                    }
                }
            }
        })
    }

    // Show message:
    private fun showToast(task : Task<Void>, message : String) {
        if (task.isSuccessful) Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        else Toast.makeText(requireActivity(), task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
    }

    // Change color:
    private fun changeFABColor(fab : FloatingActionButton, givenColor: Int) {
        fab.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), givenColor)
    }
}