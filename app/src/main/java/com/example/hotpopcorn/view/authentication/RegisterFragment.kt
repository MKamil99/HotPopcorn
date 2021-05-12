package com.example.hotpopcorn.view.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hotpopcorn.R

class RegisterFragment : AbstractAuthFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Displaying proper info:
        binding.tvScreenTitle.text = getString(R.string.register_screen_title)
        binding.btnConfirm.text = getString(R.string.register_screen_button)
        binding.tvQuestion.text = getString(R.string.register_screen_question)
        binding.tvAnswer.text = getString(R.string.register_screen_answer)
        binding.tilPasswordRepeat.visibility = View.VISIBLE

        // Registering process:
        binding.btnConfirm.setOnClickListener {
            loginOrRegister("register", getString(R.string.registered))
        }

        // Going back to Login Screen:
        binding.tvAnswer.setOnClickListener { requireActivity().onBackPressed() }

        return binding.root
    }
}