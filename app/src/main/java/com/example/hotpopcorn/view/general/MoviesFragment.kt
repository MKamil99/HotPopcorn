package com.example.hotpopcorn.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.model.Movie
import com.example.hotpopcorn.view.adapters.MovieListAdapter
import com.example.hotpopcorn.viewmodel.MovieViewModel

class MoviesFragment : AbstractGeneralFragment() {
    private lateinit var movieVM : MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        movieVM.moviesWithMatchingTitle.observe(viewLifecycleOwner, {
            displayNewData(movieVM.moviesWithMatchingTitle.value ?: listOf()) })
        return binding.root
    }

    private fun displayNewData(movies : List<Movie>) {
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = MovieListAdapter(movies, movieVM)
        }
    }
}