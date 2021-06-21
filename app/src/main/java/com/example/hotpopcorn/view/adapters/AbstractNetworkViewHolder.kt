package com.example.hotpopcorn.view.adapters

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpopcorn.R

abstract class AbstractNetworkViewHolder(root : View) : RecyclerView.ViewHolder(root) {
    // Checking connection with the internet:
    private fun isConnectedToInternet(context: Context) : Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
    }

    // Showing message:
    private fun showToast(context: Context) {
        Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
    }

    // Executing code after checking internet connection:
    protected fun makeIfConnected(context: Context, actionToMake: () -> Unit) {
        if (isConnectedToInternet(context)) actionToMake()
        else showToast(context)
    }
}