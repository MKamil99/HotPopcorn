package com.example.hotpopcorn.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.Person

class PersonListAdapter(private val people : List<Person>) : RecyclerView.Adapter<PersonListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(people[position])
    override fun getItemCount(): Int = people.size

    inner class ViewHolder(private val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Person) {
            with(binding) {
                tvTitleOrName.text = item.name
                val url = "https://image.tmdb.org/t/p/w185${item.profile_path}"
                Glide.with(root).load(url).centerCrop().into(ivPosterOrPhoto)
            }
        }
    }
}