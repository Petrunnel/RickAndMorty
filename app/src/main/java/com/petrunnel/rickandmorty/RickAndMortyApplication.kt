package com.petrunnel.rickandmorty

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RickAndMortyApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: RickAndMortyApplication
        val networkStateLiveData = MutableLiveData<Boolean>()

        @Suppress("unused")
        fun applicationContext(): Context {
            return instance.applicationContext
        }

        fun isNetworkAvailable() : Boolean {
            return networkStateLiveData.value == true
        }
    }

    override fun onCreate() {
        super.onCreate()
        startObserveConnectivityState()
    }

    private fun startObserveConnectivityState() {
        val connectivityManager =
            applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        connectivityManager?.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            object : ConnectivityManager.NetworkCallback() {
                private val mainHandler = Handler(Looper.getMainLooper())
                private val connectedNetworks = HashSet<Long>()

                override fun onAvailable(network: Network) {
                    if (connectedNetworks.isEmpty()) {
                        networkStateLiveData.postValue(true)
                    }
                    connectedNetworks.add(network.networkHandle)
                }

                override fun onLost(network: Network) {
                    connectedNetworks.remove(network.networkHandle)
                    /* We made delay, because in some cases when device change network type (like from wifi to cellular)
                       manager sends network lost and network available events */
                    mainHandler.postDelayed({
                        if (connectedNetworks.isEmpty()) {
                            networkStateLiveData.postValue(false)
                        }
                    }, 300)
                }
            }
        )
    }
}