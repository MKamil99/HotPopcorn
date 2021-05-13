package com.example.hotpopcorn.view.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hotpopcorn.R

class LoginFragment : AbstractAuthFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Displaying proper info:
        binding.btnConfirm.text = getString(R.string.login_screen_button)
        binding.tvQuestion.text = getString(R.string.login_screen_question)
        binding.tvAnswer.text = getString(R.string.login_screen_answer)
        binding.tilPasswordRepeat.visibility = View.GONE
        displayLoginAndPassword()

        // Logging process:
        binding.btnConfirm.setOnClickListener {
            loginOrRegister("login", getString(R.string.logged_in))
        }

        // Going to Register Screen:
        binding.tvAnswer.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
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