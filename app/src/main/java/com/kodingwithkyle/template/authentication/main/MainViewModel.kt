package com.kodingwithkyle.template.authentication.main

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodingwithkyle.template.authentication.data.models.User
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import com.kodingwithkyle.template.authentication.services.AuthenticationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel internal constructor(
    private val userRepo: UserRepo,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    val self = userRepo.fetchSelf()
    val shouldNavigateToLogin = MutableLiveData(false)

    fun handleLogoutButtonClick(self: User) {
        if (isInternetAvailable()) {
            val service = AuthenticationService.AuthServiceCreator.newService()
            viewModelScope.launch(Dispatchers.IO) {
                val response = service.logout(self.authToken!!)
                if (response.isSuccessful) {
                    val newSelf = User(self._id, self.email, "", false)
                    userRepo.insertUser(newSelf)
                    shouldNavigateToLogin.postValue(true)
                } else {
                    // show error message, could not logout
                }
            }
        } else {
            // show error message, no internet available
        }
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