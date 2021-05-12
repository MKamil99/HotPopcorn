package com.example.hotpopcorn.view.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.hotpopcorn.R
import com.example.hotpopcorn.view.general.MoviesFragment
import com.example.hotpopcorn.view.general.PeopleFragment
import com.example.hotpopcorn.view.general.TVShowsFragment
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.PersonViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class ExploreFragment : AbstractMainFragment() {
    private lateinit var movieVM : MovieViewModel
    private lateinit var personVM : PersonViewModel
    private lateinit var showVM : TVShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)

        // Initializing the lists:
        movieVM.setMoviesWithMatchingTitle("")
        personVM.setPeopleWithMatchingName("")
        showVM.setTVShowsWithMatchingTitle("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installPager(listOf(MoviesFragment(), PeopleFragment(), TVShowsFragment()),
            listOf(getString(R.string.explore_movies_tab), getString(R.string.explore_people_tab), getString(R.string.explore_shows_tab)))
    }

    // Modifying functionality of the menu:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Search View:
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.explore_searchbar_hint)
        searchView.inputType = 33 // eliminates "SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length" error
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(givenText : String) : Boolean {
                // TODO: Save the value so it will be here after coming back from certain title
                return false
            }

            override fun onQueryTextChange(givenText : String): Boolean {
                movieVM.setMoviesWithMatchingTitle(givenText)
                personVM.setPeopleWithMatchingName(givenText)
                showVM.setTVShowsWithMatchingTitle(givenText)
                return false
            }
        })
    }
}
