package com.example.hotpopcorn.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentDetailsBinding
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.view.adapters.PeopleInMovieAndTVShowAdapter
import com.example.hotpopcorn.viewmodel.CompanyViewModel
import com.example.hotpopcorn.viewmodel.PersonViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class TVShowDetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var showVM : TVShowViewModel
    private lateinit var personVM : PersonViewModel
    private lateinit var companyVM : CompanyViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout and VMs:
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)
        companyVM = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)

        // Displaying proper headers:
        binding.tvHeader1.text = getString(R.string.description_header)
        binding.tvHeader2.text = getString(R.string.cast_header)
        binding.tvHeader3.text = getString(R.string.crew_header)

        // Displaying current movie's data in TextViews and ImageView:
        showVM.currentTVShow.observe(viewLifecycleOwner, {
            // Title:
            binding.tvTitleOrName.text = it.name

            // Release date:
            if (!it.first_air_date.isNullOrEmpty())
            {
                binding.tvYear.text = "First episode air date: ${it.first_air_date}"
                binding.tvYear.visibility = View.VISIBLE
            }
            else binding.tvYear.visibility = View.GONE

            // Runtime:
            if (!it.episode_run_time.isNullOrEmpty())
            {
                var runtimeSum = 0
                it.episode_run_time.forEach { x -> runtimeSum += x }
                binding.tvRuntime.text = "Episode runtime: ${runtimeSum / it.episode_run_time.size} minutes"
                binding.tvRuntime.visibility = View.VISIBLE
            }
            else binding.tvRuntime.visibility = View.GONE

            // Genres (max two):
            if (!it.genres.isNullOrEmpty())
            {
                var genresText = ""
                var i = 0
                while (i < it.genres.size)
                {
                    if (i == 2) break
                    else genresText += it.genres[i].name + ", "
                    i += 1
                }
                binding.tvGenresOrKnownFor.text = "Genres: ${genresText.slice(IntRange(0, genresText.length - 3))}"
                binding.tvGenresOrKnownFor.visibility = View.VISIBLE
            }
            else binding.tvGenresOrKnownFor.visibility = View.GONE

            // Languages (main two but with "..." if there are more):
            if (!it.spoken_languages.isNullOrEmpty())
            {
                var languagesText = ""
                var i = 0
                while (i < it.spoken_languages.size)
                {
                    if (i == 2) { languagesText += "..." + ", "; break }
                    else languagesText += it.spoken_languages[i].english_name + ", "
                    i += 1
                }
                binding.tvOrigin.text = "Languages: ${languagesText.slice(IntRange(0, languagesText.length - 3))}"
                binding.tvOrigin.visibility = View.VISIBLE
            }
            else binding.tvOrigin.visibility = View.GONE

            // Main company:
            if (!it.production_companies.isNullOrEmpty())
            {
                val currentCompany = it.production_companies[0]
                binding.tvMainCompany.text = "Company: ${currentCompany.name}"
                binding.tvMainCompany.visibility = View.VISIBLE
                binding.tvMainCompany.setOnClickListener {
                    companyVM.setCurrentCompany(currentCompany.id)
                    binding.root.findNavController().navigate(R.id.action_TVShowDetailsFragment_to_companyDetailsFragment)
                }
            }
            else binding.tvMainCompany.visibility = View.GONE

            // Poster:
            val url = "https://image.tmdb.org/t/p/w185${it.poster_path}"
            Glide.with(binding.root).load(url).centerCrop().placeholder(R.drawable.ic_movie_24).into(binding.ivPosterOrPhoto)

            // Average vote:
            if (it.vote_average.toString().isNotEmpty())
            {
                binding.tvAvgVote.text = "Rating: ${it.vote_average}/10.0"
                binding.tvAvgVote.visibility = View.VISIBLE
            }
            else binding.tvAvgVote.visibility = View.GONE

            // Description:
            if (it.overview.isNullOrEmpty())
            {
                binding.tvHeader1.visibility = View.GONE
                binding.tvDescription.visibility = View.GONE
            }
            else
            {
                binding.tvDescription.text = it.overview
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvHeader1.visibility = View.VISIBLE
            }

            // Updating data of people from the cast and the crew:
            showVM.setPeopleConnectedWithCurrentTVShow(it.id)
        })

        // Displaying current TV show's cast data in RecyclerView:
        showVM.currentTVShowCast.observe(viewLifecycleOwner, {
            displayNewCastData(showVM.currentTVShowCast.value ?: listOf())

            // Making sure that everything is well displayed:
            binding.tvHeader2.visibility = if(showVM.currentTVShowCast.value.isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.rvCast.visibility = if(showVM.currentTVShowCast.value.isNullOrEmpty()) View.GONE else View.VISIBLE
        })

        // Displaying current TV show's crew data in RecyclerView:
        showVM.currentTVShowCrew.observe(viewLifecycleOwner, {
            displayNewCrewData(showVM.currentTVShowCrew.value ?: listOf())

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

    private fun displayNewCastData(people : List<Person>) {
        binding.rvCast.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = PeopleInMovieAndTVShowAdapter(people, personVM, "TVShow","cast")
        }
    }
    private fun displayNewCrewData(people : List<Person>) {
        binding.rvCrew.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = PeopleInMovieAndTVShowAdapter(people, personVM, "TVShow","crew")
        }
    }
}