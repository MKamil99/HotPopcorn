package com.example.hotpopcorn.view.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import com.example.hotpopcorn.R

// Class which removes Searching Icon from all Details Fragments that inherit from it:
abstract class AbstractDetailsFragment : Fragment() {
    // Informing the fragment that there is a menu:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // Managing the menu:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Search Icon:
        menu.findItem(R.id.search)?.isVisible = false
    }
}