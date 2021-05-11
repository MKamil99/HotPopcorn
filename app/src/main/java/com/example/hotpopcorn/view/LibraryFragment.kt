package com.example.hotpopcorn.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentMainBinding
import com.example.hotpopcorn.view.general.WatchedFragment
import com.example.hotpopcorn.view.general.ToWatchFragment
import com.google.firebase.auth.FirebaseAuth

class LibraryFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout:
        _binding = FragmentMainBinding.inflate(inflater, container, false)
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
        override fun getCount(): Int = 2
        override fun getItem(position: Int): Fragment {
            return when(position) {
                1 -> WatchedFragment()
                else -> ToWatchFragment()
            }
        }
        override fun getPageTitle(position: Int): CharSequence {
            return when(position) {
                1 -> getString(R.string.library_watched_tab)
                else -> getString(R.string.library_towatch_tab)
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
        searchView.queryHint = getString(R.string.library_searchbar_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(givenText : String) : Boolean {
                // TODO: Save the value so it will be here after coming back from certain title
                return false
            }

            override fun onQueryTextChange(givenText : String): Boolean {
                // TODO: Send data to ViewModel
                return false
            }
        })

        // Logout:
        menu.findItem(R.id.logout)?.setOnMenuItemClickListener {
            logOut()
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }



    private fun logOut() {
        // Remove client-server connection with Firebase:
        FirebaseAuth.getInstance().signOut()

        // Disable auto-login:
        val preferences = activity?.getSharedPreferences(
            getString(R.string.preferenceGroupName), Context.MODE_PRIVATE)
        if (preferences != null) {
            with (preferences.edit()) {
                putBoolean(getString(R.string.preferenceStateName), false)
                apply()
            }
        }
    }
}