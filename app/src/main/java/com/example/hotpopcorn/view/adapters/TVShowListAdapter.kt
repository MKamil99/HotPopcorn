package com.example.hotpopcorn.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class TVShowListAdapter(private val shows : LiveData<List<TVShow>>,
                        private val showVM : TVShowViewModel) : RecyclerView.Adapter<TVShowListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(shows.value?.get(position))
    override fun getItemCount(): Int = shows.value?.size ?: 0

    inner class ViewHolder(private val binding: ItemRowBinding) : AbstractRowViewHolder(binding) {
        fun bind(item : TVShow?) {
            if (item != null) {
                // Displaying data:
                displayTitleOrName(item.name)
                displayReleaseDate(item.first_air_date)
                displayPosterOrPhoto(item.poster_path, R.drawable.ic_tvshow_24)

                // Navigation:
                binding.rowBackground.setOnClickListener {
                    showVM.setCurrentTVShow(item.id)
                    it.findNavController().navigate(R.id.action_exploreFragment_to_TVShowDetailsFragment)
                }
            }
        }
    }
}