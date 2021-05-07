package com.example.hotpopcorn.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hotpopcorn.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logging process:
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim { it <= ' ' }
            val password = binding.etLoginPassword.text.toString().trim { it <= ' '}
            when {
                // One of the inputs is empty:
                TextUtils.isEmpty(email) -> Toast.makeText(this, "Please enter the email.", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(password) -> Toast.makeText(this, "Please enter the password.", Toast.LENGTH_SHORT).show()

                // Everything is okay:
                else -> {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "You have logged in successfully.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Going to Register Screen:
        binding.tvLoginFooter2.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}