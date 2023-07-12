package com.vero.tasky.agenda.data.connectivitymanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.vero.tasky.agenda.domain.connectivitymanager.ConnectionHandler

class ConnectionHandlerImpl(val context: Context) : ConnectionHandler {

    override fun isConnected(): Boolean {
        val connectionHandler =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectionHandler.activeNetwork ?: return false
        val networkCapabilities =
            connectionHandler.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }

}