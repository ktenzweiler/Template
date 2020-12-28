package com.kodingwithkyle.template.authentication.base

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import com.kodingwithkyle.template.authentication.services.AuthenticationService

abstract class BaseViewModel constructor(
    private val connectivityManager: ConnectivityManager,
    val mUserRepo: UserRepo
) :
    ViewModel() {

    val mService = AuthenticationService.AuthServiceCreator.newService()
    val shouldShowProgressBar = MutableLiveData(false)

    fun isInternetAvailable(): Boolean {
        var result = false
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        result = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }
}