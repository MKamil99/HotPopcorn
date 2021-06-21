package com.example.hotpopcorn.view.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hotpopcorn.R

abstract class AbstractNetworkActivity : AppCompatActivity() {
    private lateinit var connectivityManager : ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    // Checking internet connection:
    private fun isConnected(): Boolean {
        return connectivityManager.activeNetwork != null
    }

    // Executing code only if there is an internet connection:
    protected fun makeIfConnected(actionToMake: () -> Unit) {
        if (isConnected()) actionToMake()
    }

    // Executing code only if there is no internet connection:
    protected  fun makeIfNotConnected(actionToMake: () -> Unit) {
        if (!isConnected()) actionToMake()
    }

    // Adding connectivity listener and actions that should be executed after reconnecting / disconnecting
    // (based on: https://stackoverflow.com/questions/25678216/android-internet-connectivity-change-listener):
    protected fun addConnectionListener(actionForReconnecting: () -> Unit, actionForDisconnecting: () -> Unit) {
        // Actions to make:
        val networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { actionForReconnecting() }
            override fun onLost(network: Network) { actionForDisconnecting() }
        }

        // Displaying a message if during listener installation there is no internet connection:
        makeIfNotConnected { showToast(getString(R.string.no_internet)) }

        // Registering actions for further:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }

    // Showing message:
    protected fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}