package com.example.hotpopcorn.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemTileBinding
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.viewmodel.PersonViewModel

class PeopleInMovieAndTVShowAdapter(private val personVM: PersonViewModel,
                                    private val movieOrTVShow : String,
                                    private val castOrCrew : String,
                                    private val context : Context) : RecyclerView.Adapter<PeopleInMovieAndTVShowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTileBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(people[position])
    override fun getItemCount(): Int = people.size

    inner class ViewHolder(private val binding: ItemTileBinding) : AbstractTileViewHolder(binding) {
        fun bind(item : Person) {
            // Displaying data:
            displayTitleOrName(item.name)
            displayCharacterOrDepartment(if (castOrCrew == "cast") item.character else item.department)
            displayPosterOrPhoto(item.profile_path, when(item.gender) {
                2 -> R.drawable.ic_person_24_man        // man
                1 -> R.drawable.ic_person_24_woman      // woman
                else -> R.drawable.ic_person_24_human   // unknown
            })

            // Navigation:
            binding.tileBackground.setOnClickListener {
                makeIfConnected(context) {
                    // Going to new page:
                    personVM.setCurrentPerson(item.id)
                    if (movieOrTVShow == "movie")
                        it.findNavController().navigate(R.id.action_movieDetailsFragment_to_personDetailsFragment)
                    else
                        it.findNavController().navigate(R.id.action_TVShowDetailsFragment_to_personDetailsFragment)
                }
            }
        }
    }

    // Stored data:
    private var people = emptyList<Person>()
    fun setData(newList : List<Person>) {
        people = newList
        notifyDataSetChanged()
    }
}