package com.example.hotpopcorn.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.databinding.FragmentGeneralListBinding
import com.example.hotpopcorn.model.Movie
import com.example.hotpopcorn.view.adapters.MovieListAdapter
import com.example.hotpopcorn.viewmodel.MovieViewModel

class MoviesFragment : Fragment() {
    private var _binding: FragmentGeneralListBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieVM : MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout and VM:
        _binding = FragmentGeneralListBinding.inflate(inflater, container, false)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)

        // Displaying the list:
        movieVM.moviesWithMatchingTitle.observe(viewLifecycleOwner, {
            displayNewData(movieVM.moviesWithMatchingTitle.value ?: listOf()) })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayNewData(movies : List<Movie>) {
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = MovieListAdapter(movies, movieVM)
        }
    }
}