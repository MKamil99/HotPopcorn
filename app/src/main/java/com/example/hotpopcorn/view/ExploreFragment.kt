package com.example.hotpopcorn.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentMainBinding
import com.example.hotpopcorn.view.general.MoviesFragment
import com.example.hotpopcorn.view.general.PeopleFragment
import com.example.hotpopcorn.view.general.TVShowsFragment
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.PersonViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel
import com.google.firebase.auth.FirebaseAuth

class ExploreFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieVM : MovieViewModel
    private lateinit var personVM : PersonViewModel
    private lateinit var showVM : TVShowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout and VMs:
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        movieVM = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        personVM = ViewModelProvider(requireActivity()).get(PersonViewModel::class.java)
        showVM = ViewModelProvider(requireActivity()).get(TVShowViewModel::class.java)

        // Initializing the lists:
        movieVM.setMoviesWithMatchingTitle("")
        personVM.setPeopleWithMatchingName("")
        showVM.setTVShowsWithMatchingTitle("")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    // Adding pager for inner fragments:
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabLayout.setupWithViewPager(binding.pager)
        viewPagerAdapter = PagerAdapter(this.childFragmentManager)
        binding.pager.adapter = viewPagerAdapter
    }

    // Managing inner fragments:
    private lateinit var viewPagerAdapter: PagerAdapter
    inner class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = 3
        override fun getItem(position: Int): Fragment {
            return when(position) {
                1 -> PeopleFragment()
                2 -> TVShowsFragment()
                else -> MoviesFragment()
            }
        }
        override fun getPageTitle(position: Int): CharSequence {
            return when(position) {
                1 -> getString(R.string.explore_people_tab)
                2 -> getString(R.string.explore_shows_tab)
                else -> getString(R.string.explore_movies_tab)
            }
        }
    }



    // Informing the fragment that there is a menu:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // Managing the menu:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_bar_menu, menu)

        // Search View:
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = "What are you looking for?"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(givenText : String) : Boolean {
                // Save the value so it will be here after coming back from certain title...
                return false
            }

            override fun onQueryTextChange(givenText : String): Boolean {
                movieVM.setMoviesWithMatchingTitle(givenText)
                personVM.setPeopleWithMatchingName(givenText)
                showVM.setTVShowsWithMatchingTitle(givenText)
                return false
            }
        })

        // Logout:
        menu.findItem(R.id.logout)?.setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
}
