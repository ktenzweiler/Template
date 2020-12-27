package com.kodingwithkyle.template.authentication.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.kodingwithkyle.template.R
import com.kodingwithkyle.template.authentication.base.BaseFragment
import com.kodingwithkyle.template.authentication.data.AppDatabase
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import com.kodingwithkyle.template.authentication.main.MainActivity
import com.kodingwithkyle.template.authentication.register.RegistrationFragment

class SignInFragment : BaseFragment() {

    companion object {
        const val TAG = "SignInFragment"
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by viewModels {
        SignInVMFactory(
            UserRepo.getInstance(AppDatabase.getInstance(requireContext()).userDao()),
            connectivityManager
        )
    }

    private lateinit var mSignInButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.sign_in_fragment, container, false)

        mSignInButton = view.findViewById(R.id.sign_in_btn)

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

        mSignInButton.setOnClickListener {
            viewModel.handleSignInButtonClick()
        }

        view.findViewById<Button>(R.id.register_btn).setOnClickListener {
            viewModel.handleRegisterButtonClick()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.isSignInButtonEnabled.observe(viewLifecycleOwner) {
            mSignInButton.isEnabled = it
        }
        viewModel.shouldNavigateToRegisterScreen.observe(viewLifecycleOwner) {
            if (it) {
                parentFragmentManager.run {
                    beginTransaction()
                        .add(R.id.container, RegistrationFragment.newInstance())
                        .addToBackStack(RegistrationFragment.TAG)
                        .commit()
                }
            }
        }
        viewModel.self.observe(viewLifecycleOwner) {
            it?.let {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}