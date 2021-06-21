package com.example.hotpopcorn.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import com.example.hotpopcorn.R
import com.example.hotpopcorn.model.SavedObject
import com.example.hotpopcorn.view.authentication.AuthActivity
import com.example.hotpopcorn.viewmodel.FirebaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

abstract class AbstractFirebaseActivity : AbstractNetworkActivity() {
    protected lateinit var firebaseVM : FirebaseViewModel
    private lateinit var myAccount : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializing ViewModel:
        firebaseVM = ViewModelProvider(this).get(FirebaseViewModel::class.java)

        // Saving database reference and starting to listen to it:
        myAccount = FirebaseAuth.getInstance()
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
    }

    // Managing the menu - LogOut Icon:
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Menu layout:
        menuInflater.inflate(R.menu.top_bar_menu, menu)

        // Logout Icon with logging out process:
        menu?.findItem(R.id.logout)?.setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            true
        }

        return true
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
            override fun onCancelled(error: DatabaseError) {
                if (myAccount.currentUser != null)
                    showToast(error.message)
            }
        })
    }
}