package com.example.hotpopcorn.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ActivityMainBinding
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.PersonViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel

class MainActivity : AbstractFirebaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var movieVM : MovieViewModel
    private lateinit var personVM : PersonViewModel
    private lateinit var showVM : TVShowViewModel
    private var searchbarContent : String = ""
    private var justLaunched : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding with layout:
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()

        // Initializing ViewModels:
        movieVM = ViewModelProvider(this).get(MovieViewModel::class.java)
        personVM = ViewModelProvider(this).get(PersonViewModel::class.java)
        showVM = ViewModelProvider(this).get(TVShowViewModel::class.java)

        // Adding connection listener and initializing the lists:
        makeIfNotConnected { justLaunched = false }
        addConnectionListener(
            actionForReconnecting = {
                downloadData(searchbarContent)
                if (!justLaunched) showToast(getString(R.string.connected))
                justLaunched = false
            }, actionForDisconnecting = {
                showToast(getString(R.string.disconnected))
            }
        )

        // Displaying subtitle and Home Icon only in Details:
        val bar = (this as AppCompatActivity?)?.supportActionBar
        bar?.setHomeAsUpIndicator(R.drawable.ic_home_24)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.exploreFragment || destination.id == R.id.libraryFragment) {
                bar?.subtitle = null
                bar?.setDisplayHomeAsUpEnabled(false)
            } else bar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    // Managing the menu - Search Icon:
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        // Search Icon with searching process:
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.inputType = 33 // eliminates "SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length" error
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(givenText : String) : Boolean { return false }
            override fun onQueryTextChange(givenText : String): Boolean {
                makeIfConnected {
                    searchbarContent = givenText
                    downloadData(searchbarContent)
                }
                return false
            }
        })

        return true
    }

    // Managing the Home Button:
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
        }

        return super.onOptionsItemSelected(item)
    }

    // Setting up navigation that can be seen at the bottom of the screen:
    private fun setUpNavigation() {
        // Functionality:
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        NavigationUI.setupWithNavController(binding.btmNav, navHostFragment.navController)

        // Hiding in Details:
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.exploreFragment || destination.id == R.id.libraryFragment)
                binding.btmNav.visibility = View.VISIBLE
            else binding.btmNav.visibility = View.GONE
        }
    }

    // Downloading matching data for all lists:
    private fun downloadData(givenText : String) {
        movieVM.setMoviesWithMatchingTitle(givenText)
        personVM.setPeopleWithMatchingName(givenText)
        showVM.setTVShowsWithMatchingTitle(givenText)
        firebaseVM.setMatchingSavedObjects(givenText)
    }
}