package com.example.daytask.presentation.authentication.presentation.events

import com.google.firebase.auth.AuthCredential

sealed interface LoginEvent {

    data class OnEmailChange(val newEmail: String): LoginEvent
    data class OnPasswordChange(val newPassword: String): LoginEvent
    data object Login: LoginEvent
    data class LoginWithGoogle(val authCredential: AuthCredential): LoginEvent
}