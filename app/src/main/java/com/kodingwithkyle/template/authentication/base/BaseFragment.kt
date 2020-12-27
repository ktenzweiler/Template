package com.kodingwithkyle.template.authentication.base

import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    val connectivityManager: ConnectivityManager by lazy {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}