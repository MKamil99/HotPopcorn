package com.example.hotpopcorn.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemTileBinding
import com.example.hotpopcorn.model.GeneralObject
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class MoviesAndTVShowsInPersonAdapter(private val moviesAndTVShows : LiveData<List<GeneralObject>>,
                                      private val movieVM : MovieViewModel,
                                      private val showVM: TVShowViewModel,
                                      private val inCastOrInCrew : String) : RecyclerView.Adapter<MoviesAndTVShowsInPersonAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTileBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(moviesAndTVShows.value?.get(position))
    override fun getItemCount(): Int = moviesAndTVShows.value?.size ?: 0

    inner class ViewHolder(private val binding: ItemTileBinding) : AbstractTileViewHolder(binding) {
        fun bind(item : GeneralObject?) {
            if (item != null) {
                // Displaying data:
                displayTitleOrName(if (item.media_type == "movie") item.title else item.name)
                displayCharacterOrDepartment(if (inCastOrInCrew == "inCast") item.character else item.department)
                displayPosterOrPhoto(item.poster_path, if (item.media_type == "movie") R.drawable.ic_movie_24 else R.drawable.ic_tvshow_24)

                // Navigation:
                binding.tileBackground.setOnClickListener {
                    if (item.media_type == "movie") {
                        movieVM.setCurrentMovie(item.id)
                        it.findNavController().navigate(R.id.action_personDetailsFragment_to_movieDetailsFragment)
                    } else {
                        showVM.setCurrentTVShow(item.id)
                        it.findNavController().navigate(R.id.action_personDetailsFragment_to_TVShowDetailsFragment)
                    }
                }
            }
        }
    }
}