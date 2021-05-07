package com.example.hotpopcorn.view.details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
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

class PersonDetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var personVM : PersonViewModel
    private lateinit var movieVM : MovieViewModel
    private lateinit var showVM : TVShowViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout and VMs:
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)

        // Displaying proper headers:
        binding.tvHeader1.text = getString(R.string.biography_header)
        binding.tvHeader1.visibility = View.VISIBLE
        binding.tvHeader2.text = getString(R.string.incast_header)
        binding.tvHeader2.visibility = View.VISIBLE
        binding.tvHeader3.text = getString(R.string.increw_header)
        binding.tvHeader3.visibility = View.VISIBLE

        // Displaying current person's data in TextViews and ImageView:
        personVM.currentPerson.observe(viewLifecycleOwner, {
            // Name:
            binding.tvTitleOrName.text = it.name

            // Date of birth:
            if (it.birthday.isNullOrEmpty()) binding.tvYear.visibility = View.GONE
            else {
                binding.tvYear.text = "Born: ${it.birthday}"

                // Age:
                if (it.deathDay.isNullOrEmpty()) {
                    val today = LocalDate.now()
                    val then = LocalDate.parse(it.birthday)
                    val diff = ChronoUnit.YEARS.between(then, today)
                    binding.tvYear.text = binding.tvYear.text.toString() + " (${diff} years old)"
                }

                binding.tvYear.visibility = View.VISIBLE
            }

            // Date of death:
            if (it.deathDay.isNullOrEmpty() || it.birthday.isNullOrEmpty()) binding.tvYear2.visibility = View.GONE
            else {
                binding.tvYear2.text = "Died: ${it.deathDay}"
                binding.tvYear2.visibility = View.VISIBLE
            }

            // Runtime:
            binding.tvRuntime.visibility = View.GONE

            // Place of birth:
            binding.tvOrigin.text = "Place of Birth: " + it.place_of_birth
            if (it.place_of_birth.isNullOrEmpty()) binding.tvOrigin.visibility = View.GONE
            else binding.tvOrigin.visibility = View.VISIBLE

            // Department:
            binding.tvGenresOrKnownFor.text = "Known for: " + it.known_for_department
            if (it.known_for_department.isNullOrEmpty()) binding.tvGenresOrKnownFor.visibility = View.GONE
            else binding.tvGenresOrKnownFor.visibility = View.VISIBLE

            // Photo:
            val url = "https://image.tmdb.org/t/p/w185${it.profile_path}"
            val placeholderImg: Int = when (it.gender) {
                2 -> R.drawable.ic_person_24_man        // man
                1 -> R.drawable.ic_person_24_woman      // woman
                else -> R.drawable.ic_person_24_human   // unknown
            }
            Glide.with(binding.root).load(url).centerCrop().placeholder(placeholderImg).into(binding.ivPosterOrPhoto)

            // Average vote:
            binding.tvAvgVote.visibility = View.GONE

            // Biography:
            if (it.biography.isNullOrEmpty()) {
                binding.tvHeader1.visibility = View.GONE
                binding.tvDescription.visibility = View.GONE
            } else {
                binding.tvDescription.text = it.biography
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvHeader1.visibility = View.VISIBLE
            }

            // Button for saving:
            binding.btnSave.visibility = View.GONE

            // Updating data of movie and TV shows that current person performed in or was in crew of:
            personVM.setCurrentPersonCollection(it.id)
        })

        // Displaying current person's inCast data in RecyclerView:
        personVM.currentPersonInCastCollection.observe(viewLifecycleOwner, {
            displayNewData(personVM.currentPersonInCastCollection.value ?: listOf(), binding.rvCast, "inCast")

            // Making sure that everything is well displayed:
            if (personVM.currentPersonInCastCollection.value.isNullOrEmpty()) {
                binding.tvHeader2.visibility = View.GONE
                binding.rvCast.visibility = View.GONE
            } else {
                binding.tvHeader2.visibility = View.VISIBLE
                binding.rvCast.visibility = View.VISIBLE

                if (personVM.currentPerson.value?.biography.isNullOrEmpty()) {
                    binding.tvHeader1.text = binding.tvHeader2.text
                    binding.tvHeader1.visibility = View.VISIBLE
                    binding.tvHeader2.visibility = View.GONE
                } else binding.tvHeader1.text = resources.getString(R.string.biography_header)
            }
        })

        // Displaying current person's inCrew data in RecyclerView:
        personVM.currentPersonInCrewCollection.observe(viewLifecycleOwner, {
            displayNewData(personVM.currentPersonInCrewCollection.value ?: listOf(), binding.rvCrew, "inCrew")

            // Making sure that everything is well displayed:
            if (personVM.currentPersonInCrewCollection.value.isNullOrEmpty()) {
                binding.tvHeader3.visibility = View.GONE
                binding.rvCrew.visibility = View.GONE
            } else {
                binding.tvHeader3.visibility = View.VISIBLE
                binding.rvCrew.visibility = View.VISIBLE

                if (personVM.currentPerson.value?.biography.isNullOrEmpty() && personVM.currentPersonInCastCollection.value.isNullOrEmpty()) {
                    binding.tvHeader1.text = binding.tvHeader3.text
                    binding.tvHeader1.visibility = View.VISIBLE
                    binding.tvHeader3.visibility = View.GONE
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayNewData(moviesAndTVShows: List<GeneralObject>, recyclerView : RecyclerView, inCastOrInCrew: String) {
        recyclerView.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = MoviesAndTVShowsInPersonAdapter(moviesAndTVShows, movieVM, showVM, inCastOrInCrew)
        }
    }
}