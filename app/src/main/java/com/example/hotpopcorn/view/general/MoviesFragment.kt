package com.example.hotpopcorn.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.view.adapters.MovieListAdapter
import com.example.hotpopcorn.viewmodel.MovieViewModel

class MoviesFragment : AbstractGeneralFragment() {
    private lateinit var movieVM : MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)

        // Adding layout and adapter to RecyclerView:
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = MovieListAdapter(movieVM.moviesWithMatchingTitle, movieVM)
        }

        // Starting observing to update at runtime:
        movieVM.moviesWithMatchingTitle.observe(viewLifecycleOwner, {
            binding.rvGeneralList.adapter?.notifyDataSetChanged()
        })

        return binding.root
    }
}