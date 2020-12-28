package com.kodingwithkyle.template.authentication.base

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kodingwithkyle.template.authentication.data.models.ErrorMessage
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import com.kodingwithkyle.template.authentication.services.AuthenticationService
import okhttp3.ResponseBody

abstract class BaseViewModel constructor(
    private val connectivityManager: ConnectivityManager,
    val mUserRepo: UserRepo
) :
    ViewModel() {

    val mService = AuthenticationService.AuthServiceCreator.newService()
    val shouldShowProgressBar = MutableLiveData(false)
    val shouldShowErrorDialog = MutableLiveData<ErrorMessage>()

    fun isInternetAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun parseErrorMessage(responseBody: ResponseBody?): ErrorMessage? {
        val type = object : TypeToken<ErrorMessage>() {}.type
        return Gson().fromJson(responseBody?.charStream(), type)
    }
}