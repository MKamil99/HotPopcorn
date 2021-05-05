package com.example.hotpopcorn.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.databinding.FragmentGeneralListBinding
import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.view.adapters.TVShowListAdapter
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class TVShowsFragment : Fragment() {
    private var _binding: FragmentGeneralListBinding? = null
    private val binding get() = _binding!!

    private lateinit var showVM : TVShowViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Initializing the list:
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)
        showVM.setTVShowsWithMatchingTitle("")
        showVM.TVShowsWithMatchingTitle.observe(viewLifecycleOwner, {
            displayNewData(showVM.TVShowsWithMatchingTitle.value ?: listOf()) })

        // Access to layout:
        _binding = FragmentGeneralListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayNewData(shows : List<TVShow>) {
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = TVShowListAdapter(shows)
        }
    }
}