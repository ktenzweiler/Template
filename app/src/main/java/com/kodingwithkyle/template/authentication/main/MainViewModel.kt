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
                val response = mService.logout(self.authToken!!)
                if (response.isSuccessful) {
                    val newSelf = User(self._id, self.email, "", false)
                    mUserRepo.insertUser(newSelf)
                    shouldNavigateToLogin.postValue(true)
                } else {
                    // show error message, could not logout
                }
            }
        } else {
            // show error message, no internet available
        }
    }
}