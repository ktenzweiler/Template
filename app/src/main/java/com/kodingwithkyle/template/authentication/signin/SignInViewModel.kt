package com.kodingwithkyle.template.authentication.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodingwithkyle.template.authentication.services.AuthenticationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {

    val isSignInButtonEnabled = MutableLiveData(false)
    private var mEmailText = ""
    private var mPasswordText = ""
    val mService = AuthenticationService.AuthServiceCreator.newService()
    var mToken = ""

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

    fun handleSignInButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = mService.login(mEmailText, mPasswordText)
            if (response.isSuccessful) {
                response.body()?.let {
                    it.authToken?.let { token ->
                        mToken = token
                    }
                    Log.d("USER", "user id = ${it._id} email = ${it.email} token = ${it.authToken}")
                }
            } else {
                Log.d("USER", "Error msg = ${response.message()}")
            }
        }
    }

    fun handleLogoutClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = mService.logout(mToken)
            if (response.isSuccessful) {
                Log.d("logout", "logged out successfully")
            } else {
                Log.d("logout", "Error msg = ${response.message()}")
            }

        }
    }
}