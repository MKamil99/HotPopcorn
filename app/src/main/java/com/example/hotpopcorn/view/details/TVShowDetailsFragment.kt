package com.example.hotpopcorn.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentDetailsBinding
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.view.adapters.PeopleInMovieAndTVShowAdapter
import com.example.hotpopcorn.viewmodel.CompanyViewModel
import com.example.hotpopcorn.viewmodel.FirebaseViewModel
import com.example.hotpopcorn.viewmodel.PersonViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel
import com.google.android.gms.tasks.Task
import java.text.SimpleDateFormat
import java.util.*

class TVShowDetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var showVM : TVShowViewModel
    private lateinit var personVM : PersonViewModel
    private lateinit var companyVM : CompanyViewModel
    private lateinit var firebaseVM : FirebaseViewModel

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout and VMs:
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)
        companyVM = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
        firebaseVM = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)

        // Displaying proper headers:
        binding.tvHeader1.text = getString(R.string.description_header)
        binding.tvHeader2.text = getString(R.string.cast_header)
        binding.tvHeader3.text = getString(R.string.crew_header)

        // Displaying current movie's data in TextViews and ImageView:
        showVM.currentTVShow.observe(viewLifecycleOwner, { currentTVShow ->
            // Title:
            binding.tvTitleOrName.text = currentTVShow.name

            // Release date:
            if (!currentTVShow.first_air_date.isNullOrEmpty()) {
                binding.tvYear.text = "First episode air date: ${currentTVShow.first_air_date}"
                binding.tvYear.visibility = View.VISIBLE
            } else binding.tvYear.visibility = View.GONE

            // Runtime:
            if (!currentTVShow.episode_run_time.isNullOrEmpty()) {
                var runtimeSum = 0
                currentTVShow.episode_run_time.forEach { x -> runtimeSum += x }
                binding.tvRuntime.text = "Episode runtime: ${runtimeSum / currentTVShow.episode_run_time.size} minutes"
                binding.tvRuntime.visibility = View.VISIBLE
            } else binding.tvRuntime.visibility = View.GONE

            // Genres (max two):
            if (!currentTVShow.genres.isNullOrEmpty()) {
                var genresText = ""
                var i = 0
                while (i < currentTVShow.genres.size) {
                    if (i == 2) break
                    else genresText += currentTVShow.genres[i].name + ", "
                    i += 1
                }
                binding.tvGenresOrKnownFor.text = "Genres: ${genresText.slice(IntRange(0, genresText.length - 3))}"
                binding.tvGenresOrKnownFor.visibility = View.VISIBLE
            } else binding.tvGenresOrKnownFor.visibility = View.GONE

            // Languages (main two but with "..." if there are more):
            if (!currentTVShow.spoken_languages.isNullOrEmpty()) {
                var languagesText = ""
                var i = 0
                while (i < currentTVShow.spoken_languages.size) {
                    if (i == 2) { languagesText += "..." + ", "; break }
                    else languagesText += currentTVShow.spoken_languages[i].english_name + ", "
                    i += 1
                }
                binding.tvOrigin.text = "Languages: ${languagesText.slice(IntRange(0, languagesText.length - 3))}"
                binding.tvOrigin.visibility = View.VISIBLE
            } else binding.tvOrigin.visibility = View.GONE

            // Main company:
            if (!currentTVShow.production_companies.isNullOrEmpty()) {
                val currentCompany = currentTVShow.production_companies[0]
                binding.tvMainCompany.text = "Company: ${currentCompany.name}"
                binding.tvMainCompany.visibility = View.VISIBLE
                binding.tvMainCompany.setOnClickListener {
                    companyVM.setCurrentCompany(currentCompany.id)
                    binding.root.findNavController().navigate(R.id.action_TVShowDetailsFragment_to_companyDetailsFragment)
                }
            } else binding.tvMainCompany.visibility = View.GONE

            // Poster:
            val url = "https://image.tmdb.org/t/p/w185${currentTVShow.poster_path}"
            Glide.with(binding.root).load(url).centerCrop().placeholder(R.drawable.ic_movie_24).into(binding.ivPosterOrPhoto)

            // Average vote:
            if (currentTVShow.vote_average.toString().isNotEmpty()) {
                binding.tvAvgVote.text = "Rating: ${currentTVShow.vote_average}/10.0"
                binding.tvAvgVote.visibility = View.VISIBLE
            } else binding.tvAvgVote.visibility = View.GONE

            // Description:
            if (currentTVShow.overview.isNullOrEmpty()) {
                binding.tvHeader1.visibility = View.GONE
                binding.tvDescription.visibility = View.GONE
            } else {
                binding.tvDescription.text = currentTVShow.overview
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvHeader1.visibility = View.VISIBLE
            }

            // Floating Action Button:
            binding.btnSave.visibility = View.VISIBLE
            changeFABColor(R.color.gray)
            firebaseVM.moviesAndShowsOverall.observe(viewLifecycleOwner, {
                // Checking database:
                val savedObject = firebaseVM.moviesAndShowsOverall.value?.find { savedObject ->
                    savedObject.movieOrTVShowID == currentTVShow.id && savedObject.media_type == "tv" }
                val objectToSave = SavedObject("tv", currentTVShow.id, currentTVShow.name,
                    currentTVShow.poster_path ?: "", currentTVShow.first_air_date,
                    SimpleDateFormat("dd-MM-yyyy").format(Date()),
                    savedObject != null) // if is in the lists, move from 'To Watch' to 'Watched' (seen = true)
                val rowInDatabase = firebaseVM.currentUserRef.value?.child("tv-" + currentTVShow.id)

                // Case 1: TV Show is not in "To Watch" and not in "Watched":
                if (savedObject == null) {
                    changeFABColor(R.color.gray)
                    binding.btnSave.setOnClickListener {
                        rowInDatabase?.setValue(objectToSave)?.addOnCompleteListener {
                            showToast(it, getString(R.string.added_to_towatch))
                        }
                    }
                } else {
                    // Case 2: TV Show is in "To Watch":
                    if (savedObject.seen == false) {
                        changeFABColor(android.R.color.holo_red_dark)
                        binding.btnSave.setOnClickListener {
                            rowInDatabase?.setValue(objectToSave)?.addOnCompleteListener {
                                showToast(it, getString(R.string.added_to_watched))
                            }
                        }
                    // Case 3: TV Show is in "Watched":
                    } else {
                        changeFABColor(android.R.color.holo_green_dark)
                        binding.btnSave.setOnClickListener {
                            rowInDatabase?.removeValue()?.addOnCompleteListener {
                                showToast(it, getString(R.string.added_to_none))
                            }
                        }
                    }
                }
            })

            // Updating data of people from the cast and the crew:
            showVM.setPeopleConnectedWithCurrentTVShow(currentTVShow.id)
        })

        // Displaying current TV show's cast data in RecyclerView:
        showVM.currentTVShowCast.observe(viewLifecycleOwner, {
            displayNewData(showVM.currentTVShowCast.value ?: listOf(), binding.rvCast, "cast")

            // Making sure that everything is well displayed:
            binding.tvHeader2.visibility = if(showVM.currentTVShowCast.value.isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.rvCast.visibility = if(showVM.currentTVShowCast.value.isNullOrEmpty()) View.GONE else View.VISIBLE
        })

        // Displaying current TV show's crew data in RecyclerView:
        showVM.currentTVShowCrew.observe(viewLifecycleOwner, {
            displayNewData(showVM.currentTVShowCrew.value ?: listOf(), binding.rvCrew, "crew")

            // Making sure that everything is well displayed:
            binding.tvHeader3.visibility = if(showVM.currentTVShowCrew.value.isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.rvCrew.visibility = if(showVM.currentTVShowCrew.value.isNullOrEmpty()) View.GONE else View.VISIBLE
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayNewData(people : List<Person>, recyclerView : RecyclerView, castOrCrew: String) {
        recyclerView.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = PeopleInMovieAndTVShowAdapter(people, personVM, "TVShow", castOrCrew)
        }
    }

    private fun showToast(task : Task<Void>, message : String) {
        if (task.isSuccessful) Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        else Toast.makeText(requireActivity(), task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun changeFABColor(givenColor: Int) {
        binding.btnSave.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), givenColor)
    }
}