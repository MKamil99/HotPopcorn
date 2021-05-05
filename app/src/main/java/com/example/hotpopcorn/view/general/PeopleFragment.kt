package com.example.hotpopcorn.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpopcorn.databinding.FragmentGeneralListBinding
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.view.adapters.PersonListAdapter
import com.example.hotpopcorn.viewmodel.PersonViewModel

class PeopleFragment : Fragment() {
    private var _binding: FragmentGeneralListBinding? = null
    private val binding get() = _binding!!

    private lateinit var personVM : PersonViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Initializing the list:
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)
        personVM.setPeopleWithMatchingName("")
        personVM.peopleWithMatchingName.observe(viewLifecycleOwner, {
            displayNewData(personVM.peopleWithMatchingName.value ?: listOf()) })

        // Access to layout:
        _binding = FragmentGeneralListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayNewData(people : List<Person>) {
        binding.rvGeneralList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = PersonListAdapter(people)
        }
    }
}