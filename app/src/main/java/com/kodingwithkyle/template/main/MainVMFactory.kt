package com.kodingwithkyle.template.main

import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kodingwithkyle.template.data.repo.UserRepo

class MainVMFactory(
    private val userRepo: UserRepo,
    private val connectivityManager: ConnectivityManager
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MainViewModel(userRepo, connectivityManager) as T
}