package com.example.hotpopcorn.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ActivityMainBinding
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.view.authentication.AuthActivity
import com.example.hotpopcorn.viewmodel.FirebaseViewModel
import com.example.hotpopcorn.viewmodel.MovieViewModel
import com.example.hotpopcorn.viewmodel.PersonViewModel
import com.example.hotpopcorn.viewmodel.TVShowViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseVM : FirebaseViewModel
    private lateinit var movieVM : MovieViewModel
    private lateinit var personVM : PersonViewModel
    private lateinit var showVM : TVShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding with layout:
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()

        // Initializing ViewModels:
        firebaseVM = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        movieVM = ViewModelProvider(this).get(MovieViewModel::class.java)
        personVM = ViewModelProvider(this).get(PersonViewModel::class.java)
        showVM = ViewModelProvider(this).get(TVShowViewModel::class.java)

        // Initializing the lists:
        movieVM.setMoviesWithMatchingTitle("")
        personVM.setPeopleWithMatchingName("")
        showVM.setTVShowsWithMatchingTitle("")

        // Saving database reference and starting to listen to it:
        val myAccount = FirebaseAuth.getInstance()
        val dbReference = FirebaseDatabase.getInstance().getReference("users/${myAccount.uid}")
        firebaseVM.setCurrentUserRef(dbReference)
        addFirebaseListener(dbReference)

        // Returning to Login Screen after logging out:
        myAccount.addAuthStateListener {
            if (myAccount.currentUser == null) {
                showToast(getString(R.string.logged_out))
                startActivity(Intent(this, AuthActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
            }
        }

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

    // Managing the menu:
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Menu layout:
        menuInflater.inflate(R.menu.top_bar_menu, menu)

        // Logout Icon with logging out process:
        menu?.findItem(R.id.logout)?.setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            true
        }

        // Search Icon with searching process:
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.inputType = 33 // eliminates "SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length" error
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(givenText : String) : Boolean { return false }
            override fun onQueryTextChange(givenText : String): Boolean {
                movieVM.setMoviesWithMatchingTitle(givenText)
                personVM.setPeopleWithMatchingName(givenText)
                showVM.setTVShowsWithMatchingTitle(givenText)
                firebaseVM.setMatchingSavedObjects(givenText)
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

    // Listening to Firebase Realtime Database and saving data that will be displayed in Library Fragment:
    private fun addFirebaseListener(ref : DatabaseReference) {
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newRows = ArrayList<SavedObject>()
                for (child in snapshot.children) {
                    val newRow = child.getValue(SavedObject::class.java)
                    if (newRow != null) newRows.add(newRow)
                }
                firebaseVM.setSavedObjectsFromFirebase(newRows)
            }
            override fun onCancelled(error: DatabaseError) { showToast(error.message) }
        })
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

    // Showing message:
    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}