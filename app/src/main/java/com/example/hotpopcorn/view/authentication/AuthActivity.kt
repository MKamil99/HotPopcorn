package com.example.hotpopcorn.view.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.hotpopcorn.databinding.ActivityAuthBinding
import com.example.hotpopcorn.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Go to Main Activity, if user haven't logged out earlier:
        if (FirebaseAuth.getInstance().uid != null)
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })

        // Stay in Auth Activity and load the layout, if user have logged out earlier:
        else {
            binding = ActivityAuthBinding.inflate(layoutInflater)
            binding.navHostFragmentAuth.setOnClickListener { hideKeyboard() }
            setContentView(binding.root)
        }
    }

    // Hiding keyboard and losing focus:
    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}
