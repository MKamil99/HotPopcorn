package com.example.hotpopcorn.view.details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentDetailsBinding
import com.example.hotpopcorn.model.GeneralObject
import com.example.hotpopcorn.view.adapters.MoviesAndTVShowsInPersonAdapter
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.PersonViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class PersonDetailsFragment : AbstractDetailsFragment() {
    // Binding Fragment with ViewModels:
    private lateinit var personVM : PersonViewModel
    private lateinit var movieVM : MovieViewModel
    private lateinit var showVM : TVShowViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)
    }

    // Binding Fragment with layout:
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        displayHeaders()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personVM.currentPerson.observe(viewLifecycleOwner, { currentPerson ->
            // Displaying current person's data in TextViews and ImageView:
            binding.tvTitleOrName.text = currentPerson.name
            displayDates(currentPerson.birthday, currentPerson.deathday)
            binding.tvRuntime.visibility = View.GONE
            displayDepartment(currentPerson.known_for_department)
            displayPlaceOfBirth(currentPerson.place_of_birth)
            binding.tvMainCompany.visibility = View.GONE
            displayPhoto(currentPerson.profile_path, currentPerson.gender)
            binding.tvAvgVote.visibility = View.GONE
            displayBiography(currentPerson.biography)

            // Floating Action Button:
            binding.btnSave.visibility = View.GONE

            // Updating data of movie and TV shows that current person performed in or was in crew of:
            personVM.setCurrentPersonCollection(currentPerson.id)
        })

        // Displaying current person's inCast and inCrew data in RecyclerViews:
        initializeList(personVM.currentPersonInCastCollection, binding.rvCast, "inCast", binding.tvHeader2)
        initializeList(personVM.currentPersonInCrewCollection, binding.rvCrew, "inCrew", binding.tvHeader3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Displaying proper headers:
    private fun displayHeaders() {
        binding.tvHeader1.text = getString(R.string.biography_header)
        binding.tvHeader1.visibility = View.VISIBLE
        binding.tvHeader2.text = getString(R.string.incast_header)
        binding.tvHeader2.visibility = View.VISIBLE
        binding.tvHeader3.text = getString(R.string.increw_header)
        binding.tvHeader3.visibility = View.VISIBLE
    }

    // Displaying current person's photo:
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun displayPhoto(profilePath : String?, gender : Int) {
        val placeholderID: Int = when (gender) {
            2 -> R.drawable.ic_person_24_man        // man
            1 -> R.drawable.ic_person_24_woman      // woman
            else -> R.drawable.ic_person_24_human   // unknown
        }
        if (profilePath != null) {
            val url = "https://image.tmdb.org/t/p/w185$profilePath"
            Glide.with(binding.root).load(url).centerCrop().placeholder(placeholderID).into(binding.ivPosterOrPhoto)
        } else binding.ivPosterOrPhoto.setImageDrawable(resources.getDrawable(placeholderID, context?.theme))
    }

    // Displaying current person's days of birth and death:
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun displayDates(birthDay : String?, deathDay : String?) {
        // Date of birth:
        if (birthDay.isNullOrEmpty()) binding.tvYear.visibility = View.GONE
        else {
            binding.tvYear.text = "Born: $birthDay"

            // Age:
            if (deathDay.isNullOrEmpty()) {
                val today = LocalDate.now()
                val then = LocalDate.parse(birthDay)
                val diff = ChronoUnit.YEARS.between(then, today)
                binding.tvYear.text = binding.tvYear.text.toString() + " (${diff} years old)"
            }

            binding.tvYear.visibility = View.VISIBLE
        }

        // Date of death:
        if (deathDay.isNullOrEmpty() || birthDay.isNullOrEmpty())
            binding.tvYear2.visibility = View.GONE
        else {
            binding.tvYear2.text = "Died: $deathDay"
            binding.tvYear2.visibility = View.VISIBLE
        }
    }

    // Displaying current person's department (known-for):
    @SuppressLint("SetTextI18n")
    private fun displayDepartment(department : String?) {
        binding.tvGenresOrKnownFor.text = "Known for: $department"
        if (department.isNullOrEmpty()) binding.tvGenresOrKnownFor.visibility = View.GONE
        else binding.tvGenresOrKnownFor.visibility = View.VISIBLE
    }

    // Displaying current person's place of birth:
    @SuppressLint("SetTextI18n")
    private fun displayPlaceOfBirth(placeOfBirth : String?) {
        binding.tvOrigin.text = "Place of Birth: $placeOfBirth"
        if (placeOfBirth.isNullOrEmpty()) binding.tvOrigin.visibility = View.GONE
        else binding.tvOrigin.visibility = View.VISIBLE
    }

    // Displaying current person's biography:
    private fun displayBiography(biography : String?) {
        if (biography.isNullOrEmpty()) {
            binding.tvHeader1.visibility = View.GONE
            binding.tvDescription.visibility = View.GONE
        } else {
            binding.tvDescription.text = biography
            binding.tvDescription.visibility = View.VISIBLE
            binding.tvHeader1.visibility = View.VISIBLE
        }
    }

    // Updating data in single RecyclerView (for cast or for crew):
    private fun initializeList(listToObserve : LiveData<List<GeneralObject>>, recyclerView: RecyclerView,
                               inCastOrInCrew : String, currentHeader : TextView) {
        // Adding layout and adapter to RecyclerView:
        recyclerView.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = MoviesAndTVShowsInPersonAdapter(listToObserve, movieVM, showVM, inCastOrInCrew)
        }

        // Starting observing to update at runtime:
        listToObserve.observe(viewLifecycleOwner, {
            // Update RecyclerView:
            recyclerView.adapter?.notifyDataSetChanged()
            // Update headers:
            if (listToObserve.value.isNullOrEmpty()) {
                currentHeader.visibility = View.GONE
                recyclerView.visibility = View.GONE
            } else {
                currentHeader.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE

                // No biography:
                if ((inCastOrInCrew == "inCast" && personVM.currentPerson.value?.biography.isNullOrEmpty()) ||
                    (inCastOrInCrew == "inCrew" && personVM.currentPerson.value?.biography.isNullOrEmpty()
                            && personVM.currentPersonInCastCollection.value.isNullOrEmpty())) {
                    binding.tvHeader1.text = currentHeader.text
                    binding.tvHeader1.visibility = View.VISIBLE
                    currentHeader.visibility = View.GONE
                } else if (inCastOrInCrew == "inCast") binding.tvHeader1.text = resources.getString(R.string.biography_header)
            }
        })
    }
}