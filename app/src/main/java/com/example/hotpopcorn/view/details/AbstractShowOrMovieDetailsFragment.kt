package com.example.hotpopcorn.view.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentDetailsBinding
import com.example.hotpopcorn.model.Genre
import com.example.hotpopcorn.model.Person
import com.example.hotpopcorn.model.ProductionCompany
import com.example.hotpopcorn.model.SpokenLanguage
import com.example.hotpopcorn.view.adapters.PeopleInMovieAndTVShowAdapter
import com.example.hotpopcorn.viewmodel.CompanyViewModel
import com.example.hotpopcorn.viewmodel.PersonViewModel

// Class which adds similar layout to all Details Fragments that inherit from it:
abstract class AbstractShowOrMovieDetailsFragment : AbstractDetailsFragmentWithFAB() {
    // Binding Fragment with ViewModels:
    private lateinit var personVM : PersonViewModel
    private lateinit var companyVM : CompanyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)
        companyVM = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
    }

    // Binding Fragment with layout:
    private var _binding: FragmentDetailsBinding? = null
    protected val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        displayHeaders()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Displaying proper headers:
    private fun displayHeaders() {
        binding.tvHeader1.text = getString(R.string.description_header)
        binding.tvHeader1.visibility = View.VISIBLE
        binding.tvHeader2.text = getString(R.string.cast_header)
        binding.tvHeader2.visibility = View.VISIBLE
        binding.tvHeader3.text = getString(R.string.crew_header)
        binding.tvHeader3.visibility = View.VISIBLE
    }

    // Displaying current Movie's / TV Show's poster:
    @SuppressLint("UseCompatLoadingForDrawables")
    protected fun displayPoster(path : String?, placeholderID : Int) {
        if (path != null) {
            val url = "https://image.tmdb.org/t/p/w185$path"
            Glide.with(binding.root).load(url).centerCrop().placeholder(placeholderID).into(binding.ivPosterOrPhoto)
        } else binding.ivPosterOrPhoto.setImageDrawable(resources.getDrawable(placeholderID, context?.theme))
    }

    // Displaying current Movie's / TV Show's title:
    protected fun displayTitle(title : String?) {
        binding.tvTitleOrName.text = title
    }

    // Displaying current Movie's release date / current TV Show's first episode release date:
    @SuppressLint("SetTextI18n")
    protected fun displayReleaseDate(date : String?, mediaType: String) {
        if (!date.isNullOrEmpty()) {
            binding.tvYear.text =
                if (mediaType == "movie") "Release date: $date"
                else "First episode air date: $date"
            binding.tvYear.visibility = View.VISIBLE
        } else binding.tvYear.visibility = View.GONE
    }

    // Displaying current Movie's runtime / current TV Show's average episode runtime:
    @SuppressLint("SetTextI18n")
    protected fun displayRuntime(runtimes : List<Int?>, mediaType: String) {
        if (!runtimes.isNullOrEmpty()) {
            var runtimeSum = 0
            runtimes.forEach { x -> runtimeSum += (x ?: 0) }
            binding.tvRuntime.text =
                if (mediaType == "movie") "Runtime: ${runtimeSum / runtimes.size} minutes"
                else "Episode runtime: ${runtimeSum / runtimes.size} minutes"
            if (runtimeSum > 0) binding.tvRuntime.visibility = View.VISIBLE
        } else binding.tvRuntime.visibility = View.GONE
    }

    // Displaying 2 main current Movie's / TV Show's genres:
    @SuppressLint("SetTextI18n")
    protected fun displayGenres(genres : List<Genre>) {
        if (!genres.isNullOrEmpty()) {
            var genresText = ""
            var i = 0
            while (i < genres.size) {
                if (i == 2) break
                else genresText += genres[i].name + ", "
                i += 1
            }
            binding.tvGenresOrKnownFor.text = "Genres: ${genresText.slice(IntRange(0, genresText.length - 3))}"
            binding.tvGenresOrKnownFor.visibility = View.VISIBLE
        } else binding.tvGenresOrKnownFor.visibility = View.GONE
    }

    // Displaying 2 main current Movie's / TV Show's languages (with "..." if there are more):
    @SuppressLint("SetTextI18n")
    protected fun displayLanguages(languages : List<SpokenLanguage>) {
        if (!languages.isNullOrEmpty()) {
            var languagesText = ""
            var i = 0
            while (i < languages.size) {
                if (i == 2) {
                    languagesText += "..." + ", "
                    break
                }
                else languagesText += languages[i].english_name + ", "
                i += 1
            }
            binding.tvOrigin.text = "Languages: ${languagesText.slice(IntRange(0, languagesText.length - 3))}"
            binding.tvOrigin.visibility = View.VISIBLE
        } else binding.tvOrigin.visibility = View.GONE
    }

    // Displaying current Movie's / TV Show's main company:
    @SuppressLint("SetTextI18n")
    protected fun displayMainCompany(companies : List<ProductionCompany>, mediaType : String) {
        if (!companies.isNullOrEmpty()) {
            val currentCompany = companies[0]
            binding.tvMainCompany.text = "Company: ${currentCompany.name}"
            binding.tvMainCompany.visibility = View.VISIBLE
            binding.tvMainCompany.setOnClickListener {
                companyVM.setCurrentCompany(currentCompany.id)
                if (mediaType == "movie") binding.root.findNavController().navigate(R.id.action_movieDetailsFragment_to_companyDetailsFragment)
                else binding.root.findNavController().navigate(R.id.action_TVShowDetailsFragment_to_companyDetailsFragment)
            }
        } else binding.tvMainCompany.visibility = View.GONE
    }

    // Displaying current Movie's / TV Show's average vote:
    @SuppressLint("SetTextI18n")
    protected fun displayAverageVote(vote : Double?) {
        if (vote.toString().isNotEmpty()) {
            binding.tvAvgVote.text = "Rating: ${vote}/10.0"
            binding.tvAvgVote.visibility = View.VISIBLE
        } else binding.tvAvgVote.visibility = View.GONE
    }

    // Displaying current Movie's / TV Show's description:
    protected fun displayDescription(description : String?) {
        if (description.isNullOrEmpty()) {
            binding.tvHeader1.visibility = View.GONE
            binding.tvDescription.visibility = View.GONE
        } else {
            binding.tvDescription.text = description
            binding.tvDescription.visibility = View.VISIBLE
            binding.tvHeader1.visibility = View.VISIBLE
        }
    }

    // Adding observers to both RecyclerViews (for cast and for crew):
    protected fun observeCastAndCrew(cast : LiveData<List<Person>>,
                                     crew : LiveData<List<Person>>, movieOrTVShow: String) {
        // Displaying current movie's cast and crew data in RecyclerViews:
        initializeList(cast, binding.rvCast, movieOrTVShow, "cast", binding.tvHeader2)
        initializeList(crew, binding.rvCrew, movieOrTVShow, "crew", binding.tvHeader3)
    }

    // Updating data in single RecyclerView (for cast or for crew):
    private fun initializeList(listToObserve : LiveData<List<Person>>, recyclerView: RecyclerView,
                               movieOrTVShow : String, castOrCrew : String, header : TextView) {
        // Adding layout and adapter to RecyclerView:
        recyclerView.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = PeopleInMovieAndTVShowAdapter(listToObserve, personVM, movieOrTVShow, castOrCrew)
        }

        // Starting observing to update at runtime:
        listToObserve.observe(viewLifecycleOwner, {
            recyclerView.adapter?.notifyDataSetChanged()
            header.visibility = if (listToObserve.value.isNullOrEmpty()) View.GONE else View.VISIBLE
            recyclerView.visibility = if (listToObserve.value.isNullOrEmpty()) View.GONE else View.VISIBLE
        })
    }
}