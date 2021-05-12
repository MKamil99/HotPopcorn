package com.example.hotpopcorn.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ActivityMainBinding
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.view.authentication.AuthActivity
import com.example.hotpopcorn.viewmodel.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseVM : FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding activity with layout and VM:
        firebaseVM = ViewModelProvider(this).get(FirebaseViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()

        // Returning to Login Screen after logging out:
        val myAccount = FirebaseAuth.getInstance()
        myAccount.addAuthStateListener {
            if (myAccount.currentUser == null) {
                showToast(getString(R.string.logged_out))
                startActivity(Intent(this, AuthActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
            }
        }

        // Saving database reference:
        val myUID = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("users/$myUID")
        firebaseVM.setCurrentUserRef(ref)

        // Listening to Firebase Realtime Database:
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newRows = ArrayList<SavedObject>()
                for (child in snapshot.children) {
                    val newRow = child.getValue(SavedObject::class.java)
                    if (newRow != null) newRows.add(newRow)
                }
                firebaseVM.setSavedObjectsFromFirebase(newRows)
            }
            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
            }
        })
    }

    // Managing the menu:
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)

        // Logout Icon with logging out process:
        menu?.findItem(R.id.logout)?.setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            true
        }
        return true
    }

    // Setting up navigation that can be seen at the bottom of the screen:
    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.btmNav, navHostFragment.navController)
    }

    // Showing message:
    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}