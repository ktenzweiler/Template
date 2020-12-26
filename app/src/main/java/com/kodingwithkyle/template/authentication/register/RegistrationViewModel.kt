package com.kodingwithkyle.template.authentication.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import com.kodingwithkyle.template.authentication.services.AuthenticationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel internal constructor(private val mUserRepo: UserRepo) : ViewModel() {

    val isRegisterButtonEnabled = MutableLiveData(false)
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
        val service = AuthenticationService.AuthServiceCreator.newService()
        viewModelScope.launch(Dispatchers.IO) {
            val response = service.registerNewUser(mEmailText, mPasswordText)
            if (response.isSuccessful) {
                response.body()?.let {
                    it.isSelf = true
                    mUserRepo.insertUser(it)
                }
            } else {
                // show error message
                Log.d("USER", "Error msg = ${response.message()}")
            }
        }
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