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
            val email = binding.etEmail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' '}
            when {
                // One of the inputs is empty:
                TextUtils.isEmpty(email) -> Toast.makeText(requireActivity(), "Please enter the email.", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(password) -> Toast.makeText(requireActivity(), "Please enter the password.", Toast.LENGTH_SHORT).show()

                // Everything is okay:
                else -> {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireActivity(), "You have registered in successfully.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        } else Toast.makeText(requireActivity(), task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
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
}