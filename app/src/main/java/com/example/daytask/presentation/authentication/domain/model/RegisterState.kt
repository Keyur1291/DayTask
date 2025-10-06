package com.example.daytask.presentation.authentication.domain.model

data class RegisterState(
    val fullName:String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val terms: Boolean = false,
    val termsError: String? = null,
    val isRegistered: Boolean = false
)