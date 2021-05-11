package com.example.hotpopcorn.view.details

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import com.example.hotpopcorn.R
import com.google.firebase.auth.FirebaseAuth

// Class which adds functional logout button to all Details Fragments that inherit from it:
abstract class AbstractDetailsFragment : Fragment() {
    // Informing the fragment that there is a menu:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // Managing the menu:
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_bar_menu, menu)

        // Search Icon:
        menu.findItem(R.id.search)?.isVisible = false

        // Logout Icon:
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