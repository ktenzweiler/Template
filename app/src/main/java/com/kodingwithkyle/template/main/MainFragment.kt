package com.kodingwithkyle.template.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.kodingwithkyle.template.R
import com.kodingwithkyle.template.authentication.AuthenticationActivity
import com.kodingwithkyle.template.base.BaseFragment
import com.kodingwithkyle.template.data.AppDatabase
import com.kodingwithkyle.template.data.models.User
import com.kodingwithkyle.template.data.repo.UserRepo

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels {
        MainVMFactory(
            UserRepo.getInstance(AppDatabase.getInstance(requireContext()).userDao()),
            connectivityManager
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        mProgressLayout = view.findViewById(R.id.progress_layout)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.shouldNavigateToLogin.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(requireContext(), AuthenticationActivity::class.java)
                startActivity(intent)
            }
        }
        viewModel.shouldShowProgressBar.observe(viewLifecycleOwner) {
            if (it) {
                mProgressLayout.visibility = View.VISIBLE
            } else {
                mProgressLayout.visibility = View.GONE
            }
        }

        viewModel.shouldShowErrorDialog.observe(viewLifecycleOwner) {
            it?.let {
                showDialog("Error", it.Error)
            }
        }
    }
}