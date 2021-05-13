package com.example.hotpopcorn.view.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import com.example.hotpopcorn.R
import com.example.hotpopcorn.view.general.*

class LibraryFragment : AbstractMainFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installPager(listOf(ToWatchFragment(), WatchedFragment()),
            listOf(getString(R.string.library_towatch_tab), getString(R.string.library_watched_tab)))
    }

    // Modifying queryHint of the SearchView:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.library_searchbar_hint)
    }
}