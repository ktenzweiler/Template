package com.kodingwithkyle.template.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.kodingwithkyle.template.R
import com.kodingwithkyle.template.base.BaseFragment
import com.kodingwithkyle.template.data.AppDatabase
import com.kodingwithkyle.template.data.repo.UserRepo

class RegistrationFragment : BaseFragment() {

    companion object {
        const val TAG = "RegistrationFragment"
        fun newInstance() = RegistrationFragment()
    }

    private val viewModel: RegistrationViewModel by viewModels {
        RegistrationVMFactory(
            UserRepo(AppDatabase.getInstance(requireContext()).userDao()),
            connectivityManager
        )
    }

    private lateinit var mRegisterButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registration_fragment, container, false)
        mRegisterButton = view.findViewById(R.id.register_btn)
        mProgressLayout = view.findViewById(R.id.progress_layout)

        view.findViewById<EditText>(R.id.email_et).addTextChangedListener {
            it?.let {
                viewModel.handleEmailTextChanged(it.trim().toString())
            }
        }

        view.findViewById<EditText>(R.id.password_et).addTextChangedListener {
            it?.let {
                viewModel.handlePasswordTextChanged(it.trim().toString())
            }
        }

        view.findViewById<EditText>(R.id.confirm_password_et).addTextChangedListener {
            it?.let {
                viewModel.handleConfirmedPasswordTextChanged(it.trim().toString())
            }
        }

        mRegisterButton.setOnClickListener {
            viewModel.handleRegisterButtonClick()
        }

        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener {
            viewModel.handleCancelButtonClick()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.isRegisterButtonEnabled.observe(viewLifecycleOwner) {
            mRegisterButton.isEnabled = it
        }

        viewModel.shouldNavigateToSignInScreen.observe(viewLifecycleOwner) {
            if (it) {
                parentFragmentManager.run {
                    popBackStack()
                }
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