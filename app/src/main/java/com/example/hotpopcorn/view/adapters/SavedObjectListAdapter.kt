package com.example.hotpopcorn.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel
import java.lang.Exception

class SavedObjectListAdapter(private val savedObjects : List<SavedObject>,
                             private val movieVM : MovieViewModel,
                             private val showVM : TVShowViewModel) : RecyclerView.Adapter<SavedObjectListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(savedObjects[position])
    override fun getItemCount(): Int = savedObjects.size

    inner class ViewHolder(private val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item : SavedObject) {
            with(binding) {
                // Title and release date:
                tvTitleOrName.text = item.title
                try { tvReleaseOrBirth.text = item.releaseDate?.slice(IntRange(0,3)) }
                catch (e: Exception){ tvReleaseOrBirth.text = "" }

                // Poster:
                val url = "https://image.tmdb.org/t/p/w185${item.posterPath}"
                Glide.with(root).load(url).centerCrop().placeholder(R.drawable.ic_movie_24).into(ivPosterOrPhoto)

                // Navigation:
                binding.rowBackground.setOnClickListener {
                    if (item.movieOrTVShowID != null)
                    {
                        if (item.media_type == "movie") {
                            movieVM.setCurrentMovie(item.movieOrTVShowID)
                            it.findNavController().navigate(R.id.action_libraryFragment_to_movieDetailsFragment)
                        }
                        else {
                            showVM.setCurrentTVShow(item.movieOrTVShowID)
                            it.findNavController().navigate(R.id.action_libraryFragment_to_TVShowDetailsFragment)
                        }
                    }
                }
            }
        }
    }
}