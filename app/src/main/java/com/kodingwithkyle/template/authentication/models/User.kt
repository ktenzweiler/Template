package com.kodingwithkyle.template.authentication.models

data class User(val _id: String,
                val email: String,
                val authToken: String?)