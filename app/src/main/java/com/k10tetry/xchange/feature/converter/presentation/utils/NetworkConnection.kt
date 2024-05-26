package com.k10tetry.xchange.feature.converter.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkConnection @Inject constructor(@ApplicationContext context: Context) {

    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun isAvailable(): Boolean {
        connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)?.let { network ->
                return network.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        }
        return false
    }

}