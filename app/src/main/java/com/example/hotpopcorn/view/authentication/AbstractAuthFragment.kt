package com.example.hotpopcorn.view.authentication

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hotpopcorn.R
import com.example.hotpopcorn.view.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

// Class which adds functional authentication process and toasts to all Authentication Fragments that inherit from it:
abstract class AbstractAuthFragment : Fragment() {
    // Connecting with Firebase:
    protected fun singInOrSignUp(authenticationTask: Task<AuthResult>, email : String,
                                 password : String, repeatedPassword : String, successMessage : String) {
        when {
            // One of the inputs is empty:
            TextUtils.isEmpty(email) -> showToast(getString(R.string.missing_email))
            TextUtils.isEmpty(password) -> showToast(getString(R.string.missing_password))
            TextUtils.isEmpty(repeatedPassword) -> showToast(getString(R.string.missing_repeated_password))

            // Passwords don't match:
            password != repeatedPassword -> showToast(getString(R.string.passwords_matching_error))

            // Everything is okay:
            else -> {
                authenticationTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserForNextSessions(email, password)
                        showToast(successMessage)
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
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
            putBoolean(getString(R.string.preferenceStateName), true)
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