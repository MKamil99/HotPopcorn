package com.example.hotpopcorn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentMainBinding
import com.example.hotpopcorn.view.general.WatchedFragment
import com.example.hotpopcorn.view.general.ToWatchFragment

class LibraryFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
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
}