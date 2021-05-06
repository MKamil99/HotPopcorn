package com.example.hotpopcorn.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class TVShowListAdapter(private val shows : List<TVShow>,
                        private val showVM : TVShowViewModel) : RecyclerView.Adapter<TVShowListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(shows[position])
    override fun getItemCount(): Int = shows.size

    inner class ViewHolder(private val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item : TVShow) {
            with(binding) {
                // Title and release date:
                tvTitleOrName.text = item.name
                tvReleaseOrBirth.text = item.first_air_date.toString().slice(IntRange(0,3))

                // Poster:
                val url = "https://image.tmdb.org/t/p/w185${item.poster_path}"
                Glide.with(root).load(url).centerCrop().placeholder(R.drawable.ic_tvshow_24).into(ivPosterOrPhoto)

                // Navigation:
                binding.rowBackground.setOnClickListener {
                    showVM.setCurrentTVShow(item.id)
                    it.findNavController().navigate(R.id.action_exploreFragment_to_TVShowDetailsFragment)
                }
            }
        }
    }
}