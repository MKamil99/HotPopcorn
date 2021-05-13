package com.example.hotpopcorn.view.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.view.general.*

class ExploreFragment : AbstractMainFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installPager(listOf(MoviesFragment(), PeopleFragment(), TVShowsFragment()),
            listOf(getString(R.string.explore_movies_tab), getString(R.string.explore_people_tab), getString(R.string.explore_shows_tab)))
    }

    // Modifying queryHint of the SearchView:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.explore_searchbar_hint)
    }
}
