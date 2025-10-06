package com.example.daytask.presentation.authentication.domain.model

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoggedIn: Boolean = false
)