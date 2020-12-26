package com.kodingwithkyle.template.authentication.signin

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import com.kodingwithkyle.template.authentication.services.AuthenticationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel internal constructor(
    private val userRepo: UserRepo,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    val isSignInButtonEnabled = MutableLiveData(false)
    val shouldNavigateToRegisterScreen = MutableLiveData(false)
    val self = userRepo.fetchSelf()
    private var mEmailText = ""
    private var mPasswordText = ""
    private val mService = AuthenticationService.AuthServiceCreator.newService()

    fun handleEmailTextChanged(email: String) {
        mEmailText = email
        checkSignInButtonEnabled()
    }

    fun handlePasswordTextChanged(password: String) {
        mPasswordText = password
        checkSignInButtonEnabled()
    }

    fun handleRegisterButtonClick() {
        shouldNavigateToRegisterScreen.postValue(true)
    }

    fun handleSignInButtonClick() {
        if (isInternetAvailable()) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = mService.login(mEmailText, mPasswordText)
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.isSelf = true
                        userRepo.insertUser(it)
                    }
                } else {
                    // show failed authentication error message
                }
            }
        } else {
            // show error message, no internet available
        }
    }

    private fun checkSignInButtonEnabled() {
        isSignInButtonEnabled.postValue(
            mEmailText.contains("@")
                    and mEmailText.contains(".")
                    and (mEmailText.length >= 5)
                    and (mPasswordText.length >= 7)
        )
    }

    private fun isInternetAvailable(): Boolean {
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