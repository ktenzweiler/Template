package com.kodingwithkyle.template.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kodingwithkyle.template.authentication.data.repo.UserRepo

class RegistrationVMFactory(private val userRepo: UserRepo) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = RegistrationViewModel(userRepo) as T
}