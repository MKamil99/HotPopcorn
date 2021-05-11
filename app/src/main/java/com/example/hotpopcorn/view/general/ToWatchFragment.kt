package com.example.hotpopcorn.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.databinding.FragmentGeneralListBinding
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.view.adapters.SavedObjectListAdapter
import com.example.hotpopcorn.viewmodel.FirebaseViewModel
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class ToWatchFragment : Fragment() {
    private var _binding: FragmentGeneralListBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieVM : MovieViewModel
    private lateinit var showVM : TVShowViewModel
    private lateinit var firebaseVM : FirebaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout and VM:
        _binding = FragmentGeneralListBinding.inflate(inflater, container, false)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)
        firebaseVM = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)

        // Displaying the list:
        firebaseVM.matchingMoviesAndShowsThatAreToWatch.observe(viewLifecycleOwner, {
            displayNewData(firebaseVM.matchingMoviesAndShowsThatAreToWatch.value ?: listOf()) })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayNewData(savedObjects : List<SavedObject>) {
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = SavedObjectListAdapter(savedObjects, movieVM, showVM)
        }
    }
}