package com.example.hotpopcorn.view.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentAuthBinding
import com.example.hotpopcorn.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

// Class which adds functional authentication process, toasts and similar layout
// to all Authentication Fragments that inherit from it:
abstract class AbstractAuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    protected val binding get() = _binding!!

    // Binding with layout:
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout:
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // Connecting with Firebase:
    protected fun loginOrRegister(taskType : String, successMessage : String) {
        // Gathering values from EditTexts:
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val repeatedPassword =
            if (taskType == "login") password
            else binding.etPasswordRepeat.text.toString().trim()

        // Handling with them:
        when {
            // One of the inputs is empty:
            TextUtils.isEmpty(email) -> {
                showToast(getString(R.string.missing_email))
                binding.etEmail.requestFocus()
            }
            TextUtils.isEmpty(password) -> {
                showToast(getString(R.string.missing_password))
                binding.etPassword.requestFocus()
            }
            TextUtils.isEmpty(repeatedPassword) -> {
                showToast(getString(R.string.missing_repeated_password))
                binding.etPasswordRepeat.requestFocus()
            }

            // Passwords don't match:
            password != repeatedPassword -> showToast(getString(R.string.passwords_matching_error))

            // Everything is okay:
            else -> {
                val authenticationTask =
                    if (taskType == "login") FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    else FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

                authenticationTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserForNextSessions(email, password)
                        showToast(successMessage)
                        startActivity(Intent(requireActivity(), MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
                    } else showToast(task.exception?.message.toString())
                }
            }
        }
    }

    // Saving login and password to preferences:
    private fun saveUserForNextSessions(email : String, password : String) {
        val preferences = activity?.getSharedPreferences(
            getString(R.string.preferenceGroupName), Context.MODE_PRIVATE) ?: return
        with (preferences.edit()) {
            putString(getString(R.string.preferenceEmailName), email)
            putString(getString(R.string.preferencePasswordName), password)
            apply()
        }
    }

    // Showing message:
    private fun showToast(message : String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}