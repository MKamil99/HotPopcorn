package com.example.hotpopcorn.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.view.adapters.PersonListAdapter
import com.example.hotpopcorn.viewmodel.PersonViewModel

class PeopleFragment : AbstractGeneralFragment() {
    private lateinit var personVM : PersonViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)

        // Adding layout and adapter to RecyclerView:
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = PersonListAdapter(personVM)
        }

        // Starting observing to update at runtime:
        personVM.peopleWithMatchingName.observe(viewLifecycleOwner, {
            (binding.rvGeneralList.adapter as PersonListAdapter).setData(it) })

        return binding.root
    }
}