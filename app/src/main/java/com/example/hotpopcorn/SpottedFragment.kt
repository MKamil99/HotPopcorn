package com.example.hotpopcorn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SpottedFragment : Fragment() {
    // TO DO: declare ViewModels, Layout Managers and Adapters

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TO DO: initialize ViewModels, Layout Managers and Adapters + add observers

        return inflater.inflate(R.layout.fragment_spotted, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TO DO: Connecting recycler views with adapters and layout managers
    }
}