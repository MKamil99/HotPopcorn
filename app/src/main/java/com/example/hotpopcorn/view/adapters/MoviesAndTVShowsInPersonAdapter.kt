package com.example.hotpopcorn.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemTileBinding
import com.example.hotpopcorn.model.GeneralObject
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class MoviesAndTVShowsInPersonAdapter(private val moviesAndTVShows : List<GeneralObject>,
                                      private val movieVM : MovieViewModel,
                                      private val showVM: TVShowViewModel,
                                      private val inCastOrInCrew : String) : RecyclerView.Adapter<MoviesAndTVShowsInPersonAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTileBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(moviesAndTVShows[position])
    override fun getItemCount(): Int = moviesAndTVShows.size

    inner class ViewHolder(private val binding: ItemTileBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item : GeneralObject) {
            with(binding) {
                // Title and who was played or for what was responsible:
                tvTitleOrName.text = if (item.media_type == "movie") item.title else item.name
                tvCharacterOrDepartment.text = if (inCastOrInCrew == "inCast") item.character else item.department

                // Poster:
                val placeholderID = if (item.media_type == "movie") R.drawable.ic_movie_24 else R.drawable.ic_tvshow_24
                if (item.poster_path != null) {
                    val url = "https://image.tmdb.org/t/p/w185${item.poster_path}"
                    Glide.with(root).load(url).centerCrop().placeholder(placeholderID).into(ivPosterOrPhoto)
                } else binding.ivPosterOrPhoto.setImageDrawable(binding.root.resources.getDrawable(placeholderID, binding.root.context.theme))

                // Navigation:
                binding.tileBackground.setOnClickListener {
                    if (item.media_type == "movie")
                    {
                        movieVM.setCurrentMovie(item.id)
                        it.findNavController().navigate(R.id.action_personDetailsFragment_to_movieDetailsFragment)
                    }
                    else
                    {
                        showVM.setCurrentTVShow(item.id)
                        it.findNavController().navigate(R.id.action_personDetailsFragment_to_TVShowDetailsFragment)
                    }
                }
            }
        }
    }
}