package com.example.hotpopcorn.view.general

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.view.adapters.SavedObjectListAdapter
import com.example.hotpopcorn.viewmodel.FirebaseViewModel
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel

// Class which adds ViewModels and rendering lists to all General-Library Fragments that inherit from it:
abstract class AbstractGeneralFragmentForLibrary : AbstractGeneralFragment() {
    private lateinit var movieVM : MovieViewModel
    private lateinit var showVM : TVShowViewModel
    protected lateinit var firebaseVM : FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)
        firebaseVM = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)
    }

    protected fun addFirebaseObserverForList(listToObserve : LiveData<List<SavedObject>>) {
        listToObserve.observe(viewLifecycleOwner, {
            displayNewData(listToObserve.value ?: listOf()) })
    }

    private fun displayNewData(savedObjects : List<SavedObject>) {
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = SavedObjectListAdapter(savedObjects, movieVM, showVM)
        }
    }
}