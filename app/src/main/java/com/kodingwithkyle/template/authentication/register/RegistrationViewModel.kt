package com.kodingwithkyle.template.authentication.register

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

class RegistrationViewModel internal constructor(
    private val mUserRepo: UserRepo,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    val isRegisterButtonEnabled = MutableLiveData(false)
    val shouldNavigateToSignInScreen = MutableLiveData(false)
    private var mEmailText = ""
    private var mPasswordText = ""
    private var mConfirmedPasswordText = ""

    fun handleEmailTextChanged(email: String) {
        mEmailText = email
        checkIsRegisterButtonEnabled()
    }

    fun handlePasswordTextChanged(password: String) {
        mPasswordText = password
        checkIsRegisterButtonEnabled()
    }

    fun handleConfirmedPasswordTextChanged(confirmedPassword: String) {
        mConfirmedPasswordText = confirmedPassword
        checkIsRegisterButtonEnabled()
    }

    fun handleRegisterButtonClick() {
        if (isInternetAvailable()) {
            val service = AuthenticationService.AuthServiceCreator.newService()
            viewModelScope.launch(Dispatchers.IO) {
                val response = service.registerNewUser(mEmailText, mPasswordText)
                if (response.isSuccessful) {
                    response.body()?.let {
                        mUserRepo.insertUser(it)
                        shouldNavigateToSignInScreen.postValue(true)
                    }
                } else {
                    // show error message
                    Log.d("USER", "Error msg = ${response.message()}")
                }
            }
        } else {
            // show error message, no internet available
        }
    }

    fun handleCancelButtonClick() {
        shouldNavigateToSignInScreen.postValue(true)
    }

    private fun checkIsRegisterButtonEnabled() {
        isRegisterButtonEnabled.postValue(
            mEmailText.contains("@")
                    and mEmailText.contains(".")
                    and (mEmailText.length > 5)
                    and (mPasswordText.length > 5)
                    and (mConfirmedPasswordText == mPasswordText)
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