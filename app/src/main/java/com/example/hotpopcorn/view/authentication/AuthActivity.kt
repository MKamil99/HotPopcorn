package com.example.hotpopcorn.view.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.ActivityAuthBinding
import com.example.hotpopcorn.view.MainActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        autoLogin()
    }

    // Trying to login if isLogged is true (and other conditions):
    private fun autoLogin() {
        val rememberedIsLoggedState = this.getSharedPreferences(
            getString(R.string.preferenceGroupName), Context.MODE_PRIVATE)?.getBoolean(
            getString(R.string.preferenceStateName), false)

        val rememberedEmail = this.getSharedPreferences(
            getString(R.string.preferenceGroupName), Context.MODE_PRIVATE)?.getString(
            getString(R.string.preferenceEmailName), null)

        val rememberedPassword = this.getSharedPreferences(
            getString(R.string.preferenceGroupName), Context.MODE_PRIVATE)?.getString(
            getString(R.string.preferencePasswordName), null)

        if (rememberedIsLoggedState == true
            && !rememberedEmail.isNullOrEmpty()
            && !rememberedPassword.isNullOrEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                rememberedEmail, rememberedPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(getString(R.string.logged_in))
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else showToast(task.exception?.message.toString())
            }
        }
    }

    // Showing message:
    private fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}