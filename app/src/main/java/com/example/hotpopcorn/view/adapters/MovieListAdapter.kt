package com.example.hotpopcorn.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.Movie
import com.example.hotpopcorn.viewmodel.MovieViewModel
import java.lang.Exception

class MovieListAdapter(private val movies : List<Movie>,
                       private val movieVM : MovieViewModel) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])
    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(private val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item : Movie) {
            with(binding) {
                // Title and release date:
                tvTitleOrName.text = item.title
                try { tvReleaseOrBirth.text = item.release_date?.slice(IntRange(0,3)) }
                catch (e: Exception){ tvReleaseOrBirth.text = "" }

                // Poster:
                if (item.poster_path != null) {
                    val url = "https://image.tmdb.org/t/p/w185${item.poster_path}"
                    Glide.with(root).load(url).centerCrop().placeholder(R.drawable.ic_movie_24).into(ivPosterOrPhoto)
                } else binding.ivPosterOrPhoto.setImageDrawable(binding.root.resources.getDrawable(R.drawable.ic_movie_24, binding.root.context.theme))

                // Navigation:
                binding.rowBackground.setOnClickListener {
                    movieVM.setCurrentMovie(item.id)
                    it.findNavController().navigate(R.id.action_exploreFragment_to_movieDetailsFragment)
                }
            }
        }
    }
}