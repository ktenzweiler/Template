package com.kodingwithkyle.template.authentication.signin

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.kodingwithkyle.template.R
import com.kodingwithkyle.template.authentication.data.AppDatabase
import com.kodingwithkyle.template.authentication.data.models.User
import com.kodingwithkyle.template.authentication.data.repo.UserRepo
import com.kodingwithkyle.template.authentication.register.RegistrationFragment

class SignInFragment : Fragment() {

    companion object {
        const val TAG = "SignInFragment"
        fun newInstance() = SignInFragment()
    }

    private val connectivityManager: ConnectivityManager by lazy {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
    }
}