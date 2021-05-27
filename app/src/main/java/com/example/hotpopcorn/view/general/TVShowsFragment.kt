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
        super.onCreateView(inflater, container, savedInstanceState)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)

        // Adding layout and adapter to RecyclerView:
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = TVShowListAdapter(showVM)
        }

        // Starting observing to update at runtime:
        showVM.TVShowsWithMatchingTitle.observe(viewLifecycleOwner, {
            (binding.rvGeneralList.adapter as TVShowListAdapter).setData(it) })

        return binding.root
    }
}