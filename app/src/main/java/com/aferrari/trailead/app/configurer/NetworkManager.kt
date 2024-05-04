package com.aferrari.trailead.app.configurer

import android.content.Context
import android.net.ConnectivityManager
import java.lang.ref.WeakReference

object NetworkManager {
    private var context: WeakReference<Context>? = null

    fun init(context: Context) {
        this.context = WeakReference(context)
    }

    fun isOnline(): Boolean {
        val connectivityManager =
            context?.get()?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}