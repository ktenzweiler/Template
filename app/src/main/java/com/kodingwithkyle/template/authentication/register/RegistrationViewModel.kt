package com.kodingwithkyle.template.authentication.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {

    val isRegisterButtonEnabled = MutableLiveData(false)
    private var mEmailText = ""
    private var mPasswordText = ""
    private var mConfirmedPasswordText = ""

    fun updateEmail(email: String) {
        mEmailText = email
        checkIsRegisterButtonEnabled()
    }

    fun updatePassword(password: String) {
        mPasswordText = password
        checkIsRegisterButtonEnabled()
    }

    fun updateConfirmedPassword(confirmedPassword: String) {
        mConfirmedPasswordText = confirmedPassword
        checkIsRegisterButtonEnabled()
    }

    fun handleRegisterButtonClick() {
        // Make an API request to the backend server to register a user
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
}