package com.example.hotpopcorn.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.TVShow
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class TVShowListAdapter(private val showVM : TVShowViewModel,
                        private val context : Context) : RecyclerView.Adapter<TVShowListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(shows[position])
    override fun getItemCount(): Int = shows.size

    inner class ViewHolder(private val binding: ItemRowBinding) : AbstractRowViewHolder(binding) {
        fun bind(item : TVShow) {
            // Displaying data:
            displayTitleOrName(item.name)
            displayReleaseDate(item.first_air_date)
            displayPosterOrPhoto(item.poster_path, R.drawable.ic_tvshow_24)

            // Navigation:
            binding.rowBackground.setOnClickListener {
                makeIfConnected(context) {
                    // Going to new page:
                    showVM.setCurrentTVShow(item.id)
                    it.findNavController().navigate(R.id.action_exploreFragment_to_TVShowDetailsFragment)
                }
            }
        }
    }

    // Stored data:
    private var shows = emptyList<TVShow>()
    fun setData(newList : List<TVShow>) {
        shows = newList
        notifyDataSetChanged()
    }
}