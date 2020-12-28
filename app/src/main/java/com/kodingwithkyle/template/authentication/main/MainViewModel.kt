package com.kodingwithkyle.template.authentication.main

import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kodingwithkyle.template.authentication.base.BaseViewModel
import com.kodingwithkyle.template.authentication.data.models.User
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel internal constructor(
    userRepo: UserRepo,
    connectivityManager: ConnectivityManager
) : BaseViewModel(connectivityManager, userRepo) {

    val self = userRepo.fetchSelf()
    val shouldNavigateToLogin = MutableLiveData(false)

    fun handleLogoutButtonClick(self: User) {
        if (isInternetAvailable()) {
            viewModelScope.launch(Dispatchers.IO) {
                shouldShowProgressBar.postValue(true)
                val response = mService.logout(self.authToken!!)
                if (response.isSuccessful) {
                    mUserRepo.deleteUser(self)
                    shouldShowProgressBar.postValue(false)
                    shouldNavigateToLogin.postValue(true)
                } else {
                    // show error message, could not logout
                    shouldShowProgressBar.postValue(false)
                }
            }
        } else {
            // show error message, no internet available
        }
    }
}