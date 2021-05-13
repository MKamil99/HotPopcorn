package com.example.hotpopcorn.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ItemRowBinding
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.viewmodel.PersonViewModel

class PersonListAdapter(private val people: LiveData<List<Person>>,
                        private val personVM: PersonViewModel) : RecyclerView.Adapter<PersonListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRowBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(people.value?.get(position))
    override fun getItemCount(): Int = people.value?.size ?: 0

    inner class ViewHolder(private val binding: ItemRowBinding) : AbstractRowViewHolder(binding) {
        fun bind(item : Person?) {
            if (item != null) {
                // Displaying data:
                displayTitleOrName(item.name)
                displayPosterOrPhoto(item.profile_path, when(item.gender) {
                    2 -> R.drawable.ic_person_24_man        // man
                    1 -> R.drawable.ic_person_24_woman      // woman
                    else -> R.drawable.ic_person_24_human   // unknown
                })

                // Navigation:
                binding.rowBackground.setOnClickListener {
                    personVM.setCurrentPerson(item.id)
                    it.findNavController().navigate(R.id.action_exploreFragment_to_personDetailsFragment)
                }
            }
        }
    }
}