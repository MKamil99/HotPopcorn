package com.example.hotpopcorn.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class SavedObjectListAdapter(private val savedObjects : LiveData<List<SavedObject>>,
                             private val movieVM : MovieViewModel,
                             private val showVM : TVShowViewModel) : RecyclerView.Adapter<SavedObjectListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(savedObjects.value?.get(position))
    override fun getItemCount(): Int = savedObjects.value?.size ?: 0

    inner class ViewHolder(private val binding: ItemRowBinding) : AbstractRowViewHolder(binding) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item : SavedObject?) {
            if (item != null) {
                // Displaying data:
                displayTitleOrName(item.title)
                displayReleaseDate(item.releaseDate)
                displayPosterOrPhoto(item.posterPath, if (item.mediaType == "movie") R.drawable.ic_movie_24 else R.drawable.ic_tvshow_24)

                // Navigation:
                binding.rowBackground.setOnClickListener {
                    if (item.movieOrTVShowID != null) {
                        if (item.mediaType == "movie") {
                            movieVM.setCurrentMovie(item.movieOrTVShowID)
                            it.findNavController().navigate(R.id.action_libraryFragment_to_movieDetailsFragment)
                        } else if (item.mediaType == "tv") {
                            showVM.setCurrentTVShow(item.movieOrTVShowID)
                            it.findNavController().navigate(R.id.action_libraryFragment_to_TVShowDetailsFragment)
                        }
                    }
                }
            }
        }
    }
}