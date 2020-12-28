package com.kodingwithkyle.template.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kodingwithkyle.template.R
import com.kodingwithkyle.template.authentication.signin.SignInFragment

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SignInFragment.newInstance())
                .commitNow()
        }
    }
}