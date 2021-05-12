package com.example.hotpopcorn.view.authentication

import android.content.Intent
import android.os.Bundle
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
            setContentView(binding.root)
        }
    }
}