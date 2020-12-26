package com.kodingwithkyle.template.authentication.signin

import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kodingwithkyle.template.authentication.data.repo.UserRepo

class SignInVMFactory(
    private val userRepo: UserRepo,
    private val connectivityManager: ConnectivityManager
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        SignInViewModel(userRepo, connectivityManager) as T
}