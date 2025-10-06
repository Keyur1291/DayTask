package com.example.daytask.presentation.authentication.domain.use_case

data class RegisterUseCases(
    val validateRegisterFullName: ValidateRegisterFullName = ValidateRegisterFullName(),
    val validateRegisterEmail: ValidateRegisterEmail = ValidateRegisterEmail(),
    val validateRegisterPassword: ValidateRegisterPassword = ValidateRegisterPassword(),
    val validateRegisterTerms: ValidateRegisterTerms = ValidateRegisterTerms()
)