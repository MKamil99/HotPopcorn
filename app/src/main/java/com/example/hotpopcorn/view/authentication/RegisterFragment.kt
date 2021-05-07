package com.example.hotpopcorn.view.authentication

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
import com.example.hotpopcorn.view.MainActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout:
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        // Displaying proper info:
        binding.tvScreenTitle.text = getString(R.string.register_screen_title)
        binding.btnConfirm.text = getString(R.string.register_screen_button)
        binding.tvQuestion.text = getString(R.string.register_screen_question)
        binding.tvAnswer.text = getString(R.string.register_screen_answer)

        // Logging process:
        binding.btnConfirm.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            when {
                // One of the inputs is empty:
                TextUtils.isEmpty(email) -> showToast(getString(R.string.missing_email))
                TextUtils.isEmpty(password) -> showToast(getString(R.string.missing_password))

                // Everything is okay:
                else -> {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast(getString(R.string.registered))
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        } else showToast(task.exception?.message.toString())
                    }
                }
            }
        }

        // Going back to Login Screen:
        binding.tvAnswer.setOnClickListener { requireActivity().onBackPressed() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message : String) { Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show() }
}