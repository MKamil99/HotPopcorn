package com.example.hotpopcorn.view.details

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.example.hotpopcorn.R
import com.example.hotpopcorn.viewmodel.MovieViewModel

class MovieDetailsFragment : AbstractShowOrMovieDetailsFragment() {
    private lateinit var movieVM : MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieVM.currentMovie.observe(viewLifecycleOwner, { currentMovie ->
            // Displaying current movie's data in TextViews and ImageView:
            displayTitle(currentMovie.title)
            displayReleaseDate(currentMovie.release_date, "movie")
            displayRuntime(listOf(currentMovie.runtime), "movie")
            displayGenres(currentMovie.genres)
            displayLanguages(currentMovie.spoken_languages)
            displayMainCompany(currentMovie.production_companies, "movie")
            displayPoster(currentMovie.poster_path, R.drawable.ic_movie_24)
            displayAverageVote(currentMovie.vote_average)
            displayDescription(currentMovie.overview)

            // Floating Action Button:
            displayFAB(binding.btnSave, currentMovie.id, currentMovie.title,
                currentMovie.poster_path, currentMovie.release_date, "movie")
            addListenerToHideFAB(binding.btnSave, binding.svMainContent)

            // Updating data of people from the cast and the crew:
            movieVM.setPeopleConnectedWithCurrentMovie(currentMovie.id)
        })
        observeCastAndCrew(movieVM.currentMovieCast, movieVM.currentMovieCrew, "movie")
    }
}