package com.example.hotpopcorn.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.viewmodel.PersonViewModel

class PersonListAdapter(private val people: List<Person>,
                        private val personVM: PersonViewModel) : RecyclerView.Adapter<PersonListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(people[position])
    override fun getItemCount(): Int = people.size

    inner class ViewHolder(private val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item : Person) {
            with(binding) {
                // Name:
                tvTitleOrName.text = item.name

                // Photo:
                val placeholderID : Int = when(item.gender) {
                    2 -> R.drawable.ic_person_24_man        // man
                    1 -> R.drawable.ic_person_24_woman      // woman
                    else -> R.drawable.ic_person_24_human   // unknown
                }
                if (item.profile_path != null) {
                    val url = "https://image.tmdb.org/t/p/w185${item.profile_path}"
                    Glide.with(root).load(url).centerCrop().placeholder(placeholderID).into(ivPosterOrPhoto)
                } else binding.ivPosterOrPhoto.setImageDrawable(binding.root.resources.getDrawable(placeholderID, binding.root.context.theme))

                // Navigation:
                binding.rowBackground.setOnClickListener {
                    personVM.setCurrentPerson(item.id)
                    it.findNavController().navigate(R.id.action_exploreFragment_to_personDetailsFragment)
                }
            }
        }
    }
}