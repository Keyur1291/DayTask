package com.example.daytask.presentation.authentication.presentation.events

sealed interface RegisterEvent {

    data class OnFullNameChange(val newName: String): RegisterEvent
    data class OnEmailChange(val newEmail: String): RegisterEvent
    data class OnPasswordChange(val newPassword: String): RegisterEvent
    data class OnTermsChange(val newValue: Boolean): RegisterEvent
    data object Register: RegisterEvent
    data object RegisterWithGoogle: RegisterEvent
}