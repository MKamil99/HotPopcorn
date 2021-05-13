package com.example.hotpopcorn.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.hotpopcorn.databinding.ActivitySplashScreenBinding
import com.example.hotpopcorn.view.authentication.AuthActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding with layout:
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Changing activity after 2 seconds:
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
        }, 2000)
    }
}