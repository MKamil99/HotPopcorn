package com.example.hotpopcorn.view.details

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.hotpopcorn.R
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class TVShowDetailsFragment : AbstractShowOrMovieDetailsFragment() {
    private lateinit var showVM : TVShowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showVM.currentTVShow.observe(viewLifecycleOwner, { currentTVShow ->
            // Displaying current TV Show's title in ActionBar:
            (activity as AppCompatActivity?)?.supportActionBar?.subtitle = currentTVShow.name

            // Displaying current TV Show's data in TextViews and ImageView:
            displayTitle(currentTVShow.name)
            displayReleaseDate(currentTVShow.first_air_date, "tv")
            displayRuntime(currentTVShow.episode_run_time, "tv")
            displayGenres(currentTVShow.genres)
            displayLanguages(currentTVShow.spoken_languages)
            displayMainCompany(currentTVShow.production_companies, "tv")
            displayPoster(currentTVShow.poster_path, R.drawable.ic_tvshow_24)
            displayAverageVote(currentTVShow.vote_average)
            displayDescription(currentTVShow.overview)

            // Floating Action Button:
            displayFAB(binding.btnSave, currentTVShow.id, currentTVShow.name,
                currentTVShow.poster_path, currentTVShow.first_air_date, "tv")
            addListenerToHideFAB(binding.btnSave, binding.svMainContent)

            // Updating data of people from the cast and the crew:
            showVM.setPeopleConnectedWithCurrentTVShow(currentTVShow.id)
        })
        observeCastAndCrew(showVM.currentTVShowCast, showVM.currentTVShowCrew, "tv")
    }
}