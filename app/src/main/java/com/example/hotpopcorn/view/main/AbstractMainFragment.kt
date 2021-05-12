package com.example.hotpopcorn.view.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.hotpopcorn.databinding.FragmentMainBinding

// Class which adds similar layout to all Main Fragments that inherit from it:
abstract class AbstractMainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    protected val binding get() = _binding!!

    // Informing that there is a menu:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // Binding with layout:
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Unbinding from layout:
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Managing inner fragments:
    protected lateinit var viewPagerAdapter: PagerAdapter
    inner class PagerAdapter(fm: FragmentManager, private val fragments : List<Fragment>,
                             private val pageTitles : List<String>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = fragments.size
        override fun getItem(position: Int): Fragment = fragments[position]
        override fun getPageTitle(position: Int): CharSequence = pageTitles[position]
    }
}