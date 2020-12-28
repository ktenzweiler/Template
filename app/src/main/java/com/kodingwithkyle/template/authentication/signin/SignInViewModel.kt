package com.kodingwithkyle.template.authentication.signin

import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kodingwithkyle.template.authentication.base.BaseViewModel
import com.kodingwithkyle.template.authentication.data.models.ErrorMessage
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel internal constructor(
    userRepo: UserRepo,
    connectivityManager: ConnectivityManager
) : BaseViewModel(connectivityManager, userRepo) {

    val isSignInButtonEnabled = MutableLiveData(false)
    val shouldNavigateToRegisterScreen = MutableLiveData(false)
    val self = userRepo.fetchSelf()
    private var mEmailText = ""
    private var mPasswordText = ""

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
                shouldShowProgressBar.postValue(true)
                val response = mService.login(mEmailText, mPasswordText)
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.isSelf = true
                        mUserRepo.insertUser(it)
                    }
                    shouldShowProgressBar.postValue(false)
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody())
                    shouldShowProgressBar.postValue(false)
                    shouldShowErrorDialog.postValue(errorMessage)
                }
            }
        } else {
            shouldShowErrorDialog.postValue(ErrorMessage("No internet connection"))
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
}