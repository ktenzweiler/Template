package com.kodingwithkyle.template.authentication.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel() {

    val isSignInButtonEnabled = MutableLiveData(false)
    private var mEmailText = ""
    private var mPasswordText = ""

    fun updateEmail(email: String) {
        mEmailText = email
        checkSignInButtonEnabled()
    }

    fun updatePassword(password: String) {
        mPasswordText = password
        checkSignInButtonEnabled()
    }

    private fun checkSignInButtonEnabled() {
        isSignInButtonEnabled.postValue(
            mEmailText.contains("@")
                    and mEmailText.contains(".")
                    and (mEmailText.length > 5)
                    and (mPasswordText.length > 5)
        )
    }
}