package com.kodingwithkyle.template.authentication.register

import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kodingwithkyle.template.authentication.base.BaseViewModel
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel internal constructor(
    userRepo: UserRepo,
    connectivityManager: ConnectivityManager
) : BaseViewModel(connectivityManager, userRepo) {

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
            viewModelScope.launch(Dispatchers.IO) {
                shouldShowProgressBar.postValue(true)
                val response = mService.registerNewUser(mEmailText, mPasswordText)
                if (response.isSuccessful) {
                    response.body()?.let {
                        mUserRepo.insertUser(it)
                        shouldShowProgressBar.postValue(false)
                        shouldNavigateToSignInScreen.postValue(true)
                    }
                } else {
                    // show error message
                    Log.d("USER", "Error msg = ${response.message()}")
                    shouldShowProgressBar.postValue(false)
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
                    and (mEmailText.length >= 5)
                    and (mPasswordText.length >= 7)
                    and (mConfirmedPasswordText == mPasswordText)
        )
    }
}