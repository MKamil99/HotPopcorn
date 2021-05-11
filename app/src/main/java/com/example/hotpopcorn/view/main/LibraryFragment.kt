package com.example.hotpopcorn.view.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.hotpopcorn.R
import com.example.hotpopcorn.view.general.WatchedFragment
import com.example.hotpopcorn.view.general.ToWatchFragment
import com.example.hotpopcorn.viewmodel.FirebaseViewModel

class LibraryFragment : AbstractMainFragment() {
    private lateinit var firebaseVM: FirebaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout and VM:
        firebaseVM = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    // Adding pager for inner fragments:
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabLayout.setupWithViewPager(binding.pager)
        viewPagerAdapter = PagerAdapter(this.childFragmentManager,
            listOf(ToWatchFragment(), WatchedFragment()),
            listOf(getString(R.string.library_towatch_tab), getString(R.string.library_watched_tab)))
        binding.pager.adapter = viewPagerAdapter
    }

    // Modifying functionality of the menu:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Search View:
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.library_searchbar_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(givenText : String) : Boolean {
                // TODO: Save the value so it will be here after coming back from certain title
                return false
            }

            override fun onQueryTextChange(givenText : String): Boolean {
                firebaseVM.setMatchingSavedObjects(givenText)
                return false
            }
        })
    }
}