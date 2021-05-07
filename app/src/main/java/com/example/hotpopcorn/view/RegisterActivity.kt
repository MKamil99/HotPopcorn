package com.example.hotpopcorn.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hotpopcorn.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Registering process:
        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString().trim { it <= ' ' }
            val password = binding.etRegisterPassword.text.toString().trim { it <= ' '}
            when {
                // One of the inputs is empty:
                TextUtils.isEmpty(email) -> Toast.makeText(this, "Please enter the email.", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(password) -> Toast.makeText(this, "Please enter the password.", Toast.LENGTH_SHORT).show()

                // Everything is okay:
                else -> {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "You have registered successfully.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Going back to Login Screen:
        binding.tvRegisterFooter2.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}