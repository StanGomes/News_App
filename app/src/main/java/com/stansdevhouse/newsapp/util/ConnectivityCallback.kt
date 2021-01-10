package com.stansdevhouse.newsapp.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

class ConnectivityCallback(private val connectionStateLiveData: ConnectionStateLiveData): ConnectivityManager.NetworkCallback() {

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            connectionStateLiveData.postValue(true)
        }
    }

    override fun onLost(network: Network) {
        connectionStateLiveData.postValue(false)
    }
}