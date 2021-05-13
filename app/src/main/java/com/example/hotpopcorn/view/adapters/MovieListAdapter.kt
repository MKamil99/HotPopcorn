package com.example.hotpopcorn.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.Movie
import com.example.hotpopcorn.viewmodel.MovieViewModel

class MovieListAdapter(private val movies : LiveData<List<Movie>>,
                       private val movieVM : MovieViewModel) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies.value?.get(position))
    override fun getItemCount(): Int = movies.value?.size ?: 0

    inner class ViewHolder(private val binding: ItemRowBinding) : AbstractRowViewHolder(binding) {
        fun bind(item : Movie?) {
            if (item != null) {
                // Displaying data:
                displayTitleOrName(item.title)
                displayReleaseDate(item.release_date)
                displayPosterOrPhoto(item.poster_path, R.drawable.ic_movie_24)

                // Navigation:
                binding.rowBackground.setOnClickListener {
                    movieVM.setCurrentMovie(item.id)
                    it.findNavController().navigate(R.id.action_exploreFragment_to_movieDetailsFragment)
                }
            }
        }
    }
}