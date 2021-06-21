package com.example.hotpopcorn.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.Movie
import com.example.hotpopcorn.viewmodel.MovieViewModel

class MovieListAdapter(private val movieVM : MovieViewModel,
                       private val context : Context) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])
    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(private val binding: ItemRowBinding) : AbstractRowViewHolder(binding) {
        fun bind(item : Movie) {
            // Displaying data:
            displayTitleOrName(item.title)
            displayReleaseDate(item.release_date)
            displayPosterOrPhoto(item.poster_path, R.drawable.ic_movie_24)

            // Navigation:
            binding.rowBackground.setOnClickListener {
                makeIfConnected(context) {
                    // Going to new page:
                    movieVM.setCurrentMovie(item.id)
                    it.findNavController().navigate(R.id.action_exploreFragment_to_movieDetailsFragment)
                }
            }
        }
    }

    // Stored data:
    private var movies = emptyList<Movie>()
    fun setData(newList : List<Movie>) {
        movies = newList
        notifyDataSetChanged()
    }
}