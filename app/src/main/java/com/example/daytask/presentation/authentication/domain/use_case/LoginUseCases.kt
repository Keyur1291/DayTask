package com.example.daytask.presentation.authentication.domain.use_case

data class LoginUseCases(
    val validateLoginEmail: ValidateLoginEmail = ValidateLoginEmail(),
    val validateLoginPassword: ValidateLoginPassword = ValidateLoginPassword(),
)