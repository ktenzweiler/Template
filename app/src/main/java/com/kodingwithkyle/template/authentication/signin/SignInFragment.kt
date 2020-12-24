package com.kodingwithkyle.template.authentication.signin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.kodingwithkyle.template.R
import com.kodingwithkyle.template.authentication.register.RegistrationFragment

class SignInFragment : Fragment() {

    companion object {
        const val TAG = "SignInFragment"
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel
    private lateinit var mSignInButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.sign_in_fragment, container, false)

        mSignInButton = view.findViewById(R.id.sign_in_btn)

        view.findViewById<EditText>(R.id.email_et).addTextChangedListener {
            it?.let {
                viewModel.updateEmail(it.trim().toString())
            }
        }

        view.findViewById<EditText>(R.id.password_et).addTextChangedListener {
            it?.let {
                viewModel.updatePassword(it.trim().toString())
            }
        }

        mSignInButton.setOnClickListener {
            viewModel.handleSignInButtonClick()
        }

        view.findViewById<Button>(R.id.logout_btn).setOnClickListener {
            viewModel.handleLogoutClick()
        }

        view.findViewById<Button>(R.id.register_btn).setOnClickListener {
            fragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, RegistrationFragment.newInstance())
                    .addToBackStack(RegistrationFragment.TAG)
                    .commit()
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        viewModel.isSignInButtonEnabled.observe(viewLifecycleOwner) {
            mSignInButton.isEnabled = it
        }
    }
}