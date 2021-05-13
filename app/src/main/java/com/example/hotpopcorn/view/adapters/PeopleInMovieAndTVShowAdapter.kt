package com.example.hotpopcorn.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemTileBinding
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.viewmodel.PersonViewModel

class PeopleInMovieAndTVShowAdapter(private val people : List<Person>,
                                    private val personVM: PersonViewModel,
                                    private val movieOrTVShow : String,
                                    private val castOrCrew : String) : RecyclerView.Adapter<PeopleInMovieAndTVShowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTileBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(people[position])
    override fun getItemCount(): Int = people.size

    inner class ViewHolder(private val binding: ItemTileBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item : Person) {
            with(binding) {
                // Name and who was played or for what was responsible:
                tvTitleOrName.text = item.name
                tvCharacterOrDepartment.text = if (castOrCrew == "cast") item.character else item.department

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
                binding.tileBackground.setOnClickListener {
                    personVM.setCurrentPerson(item.id)
                    if (movieOrTVShow == "movie") it.findNavController().navigate(R.id.action_movieDetailsFragment_to_personDetailsFragment)
                    else it.findNavController().navigate(R.id.action_TVShowDetailsFragment_to_personDetailsFragment)
                }
            }
        }
    }
}