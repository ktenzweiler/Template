package com.kodingwithkyle.template.authentication.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.kodingwithkyle.template.R
import com.kodingwithkyle.template.authentication.AuthenticationActivity
import com.kodingwithkyle.template.authentication.base.BaseFragment
import com.kodingwithkyle.template.authentication.data.AppDatabase
import com.kodingwithkyle.template.authentication.data.models.User
import com.kodingwithkyle.template.authentication.data.repo.UserRepo

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

    lateinit var mUser : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        mProgressLayout = view.findViewById(R.id.progress_layout)
        view.findViewById<Button>(R.id.logout_btn).setOnClickListener {
            viewModel.handleLogoutButtonClick(mUser)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.self.observe(viewLifecycleOwner) {
            it?.let {
                mUser = it
                view?.findViewById<TextView>(R.id.email_tv)?.text = it.email
            }
        }
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
    }
}