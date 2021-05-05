package com.example.hotpopcorn.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.Movie

class MovieListAdapter(private val movies : List<Movie>) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])
    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(private val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Movie) {
            with(binding) {
                tvTitleOrName.text = item.title
                tvReleaseOrBirth.text = item.release_date.toString().slice(IntRange(0,3))
                val url = "https://image.tmdb.org/t/p/w185${item.poster_path}"
                Glide.with(root).load(url).centerCrop().into(ivPosterOrPhoto)
            }
        }
    }
}