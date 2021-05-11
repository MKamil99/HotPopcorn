package com.example.hotpopcorn.view.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentAuthBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : AbstractAuthFragment() {
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
        binding.tilPasswordRepeat.visibility = View.VISIBLE

        // Registering process:
        binding.btnConfirm.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val repeatedPassword = binding.etPasswordRepeat.text.toString().trim()
            singInOrSignUp(FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password),
                email, password, repeatedPassword, getString(R.string.registered))
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