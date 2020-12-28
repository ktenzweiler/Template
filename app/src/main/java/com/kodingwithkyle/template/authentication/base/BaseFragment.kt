package com.kodingwithkyle.template.authentication.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    lateinit var mProgressLayout: LinearLayout

    val connectivityManager: ConnectivityManager by lazy {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}