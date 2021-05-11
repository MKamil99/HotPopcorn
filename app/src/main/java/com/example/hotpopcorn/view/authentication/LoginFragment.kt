package com.example.hotpopcorn.view.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hotpopcorn.R
import com.example.hotpopcorn.databinding.FragmentAuthBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : AbstractAuthFragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Binding fragment with layout:
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        // Displaying proper info:
        binding.tvScreenTitle.text = getString(R.string.login_screen_title)
        binding.btnConfirm.text = getString(R.string.login_screen_button)
        binding.tvQuestion.text = getString(R.string.login_screen_question)
        binding.tvAnswer.text = getString(R.string.login_screen_answer)
        binding.tilPasswordRepeat.visibility = View.GONE
        displayLoginAndPassword()

        // Logging process:
        binding.btnConfirm.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            singInOrSignUp(FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password),
                email, password, password, getString(R.string.logged_in))
        }

        // Going to Register Screen:
        binding.tvAnswer.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Displaying login and password that are saved in preferences:
    private fun displayLoginAndPassword() {
        val rememberedEmail = activity?.getSharedPreferences(
            getString(R.string.preferenceGroupName), Context.MODE_PRIVATE)?.getString(
            getString(R.string.preferenceEmailName), null)
        val rememberedPassword = activity?.getSharedPreferences(
            getString(R.string.preferenceGroupName), Context.MODE_PRIVATE)?.getString(
            getString(R.string.preferencePasswordName), null)
        if (!rememberedEmail.isNullOrEmpty() && !rememberedPassword.isNullOrEmpty()) {
            binding.etEmail.setText(rememberedEmail)
            binding.etPassword.setText(rememberedPassword)
        }
    }
}