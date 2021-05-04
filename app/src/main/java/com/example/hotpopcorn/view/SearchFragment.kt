package com.example.hotpopcorn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentSearchBinding
import com.example.hotpopcorn.view.general.MoviesFragment
import com.example.hotpopcorn.view.general.PeopleFragment
import com.example.hotpopcorn.view.general.TVShowsFragment

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
                1 -> getString(R.string.search_people_tab)
                2 -> getString(R.string.search_tvshows_tab)
                else -> getString(R.string.search_movies_tab)
            }
        }
    }
}
