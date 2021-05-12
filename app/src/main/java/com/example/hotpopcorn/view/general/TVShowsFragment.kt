package com.example.hotpopcorn.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.view.adapters.TVShowListAdapter
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class TVShowsFragment : AbstractGeneralFragment() {
    private lateinit var showVM : TVShowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)
        showVM.TVShowsWithMatchingTitle.observe(viewLifecycleOwner, {
            displayNewData(showVM.TVShowsWithMatchingTitle.value ?: listOf()) })
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun displayNewData(shows : List<TVShow>) {
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = TVShowListAdapter(shows, showVM)
        }
    }
}