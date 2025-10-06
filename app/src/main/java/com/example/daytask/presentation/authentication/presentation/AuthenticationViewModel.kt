package com.example.daytask.presentation.authentication.presentation

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.presentation.authentication.domain.model.LoginState
import com.example.daytask.presentation.authentication.domain.model.RegisterState
import com.example.daytask.presentation.authentication.domain.use_case.LoginUseCases
import com.example.daytask.presentation.authentication.domain.use_case.RegisterUseCases
import com.example.daytask.presentation.authentication.presentation.events.LoginEvent
import com.example.daytask.presentation.authentication.presentation.events.RegisterEvent
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val loginUseCases: LoginUseCases = LoginUseCases(),
    private val registerUseCases: RegisterUseCases = RegisterUseCases()
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val registerValidationEventChannel = Channel<ValidationEvent>()
    val registerValidationEvent = registerValidationEventChannel.receiveAsFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState())
    val loginState = _loginState.asStateFlow()

    private val loginValidationEventChannel = Channel<ValidationEvent>()
    val loginValidationEvent = loginValidationEventChannel.receiveAsFlow()

    fun onLoginEvent(
        loginEvent: LoginEvent
    ) {

        when (loginEvent) {
            LoginEvent.Login -> {
                validateLoginData()
            }

            is LoginEvent.OnEmailChange -> {
                _loginState.update {
                    it.copy(
                        email = loginEvent.newEmail
                    )
                }
            }

            is LoginEvent.OnPasswordChange -> {
                _loginState.update {
                    it.copy(
                        password = loginEvent.newPassword
                    )
                }
            }

            is LoginEvent.LoginWithGoogle -> {
                loginWithZGoogle(loginEvent.authCredential)
            }
        }
    }

    fun onRegisterEvent(
        registerEvent: RegisterEvent
    ) {

        when (registerEvent) {
            is RegisterEvent.OnEmailChange -> {
                _registerState.update {
                    it.copy(
                        email = registerEvent.newEmail
                    )
                }
            }

            is RegisterEvent.OnFullNameChange -> {
                _registerState.update {
                    it.copy(
                        fullName = registerEvent.newName
                    )
                }
            }

            is RegisterEvent.OnPasswordChange -> {
                _registerState.update {
                    it.copy(
                        password = registerEvent.newPassword
                    )
                }
            }

            is RegisterEvent.OnTermsChange -> {
                _registerState.update {
                    it.copy(
                        terms = registerEvent.newValue
                    )
                }
            }

            RegisterEvent.Register -> {
                validateRegisterData()
            }

            RegisterEvent.RegisterWithGoogle -> {}
        }
    }


    private fun loginWithZGoogle(authCredential: AuthCredential) {

        auth.signInWithCredential(authCredential)
            .addOnSuccessListener { result ->
                viewModelScope.launch {
                    loginValidationEventChannel.send(ValidationEvent.Success)
                }
            }
    }

    private fun validateRegisterData() {

        val fullNameResult = registerUseCases.validateRegisterFullName.execute(_registerState.value.fullName)
        val emailResult = registerUseCases.validateRegisterEmail.execute(_registerState.value.email)
        val passwordResult = registerUseCases.validateRegisterPassword.execute(_registerState.value.password)
        val termsResult = registerUseCases.validateRegisterTerms.execute(_registerState.value.terms)

        val hasError = listOf(
            fullNameResult, emailResult, passwordResult, termsResult
        ).any { it.error != null }

        if (hasError) {
            _registerState.update {
                it.copy(
                    fullNameError = fullNameResult.error,
                    emailError = emailResult.error,
                    passwordError = passwordResult.error,
                    termsError = termsResult.error
                )
            }
            return
        }
        auth.createUserWithEmailAndPassword(
            _registerState.value.email,
            _registerState.value.password
        ).addOnSuccessListener {

            val user = auth.currentUser
            user?.let { firebaseUser ->

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(_registerState.value.fullName)
                    .setPhotoUri("https://pbs.twimg.com/media/EE7WUoCX4AEqONg.png".toUri())
                    .build()

                firebaseUser.updateProfile(profileUpdates).addOnSuccessListener {

                    _registerState.update {
                        it.copy(
                            fullNameError = null,
                            emailError = null,
                            passwordError = null,
                            termsError = null
                        )
                    }

                    viewModelScope.launch {
                        registerValidationEventChannel.send(ValidationEvent.Success)
                    }
                }
            }
        }.addOnFailureListener { exception ->
            _registerState.update {
                it.copy(
                    fullNameError = exception.message,
                    emailError = exception.message,
                    passwordError = exception.message,
                    termsError = exception.message
                )
            }
        }
    }

    private fun validateLoginData() {

        val emailResult = loginUseCases.validateLoginEmail.execute(_loginState.value.email)
        val passwordResult = loginUseCases.validateLoginPassword.execute(_loginState.value.password)

        val hasError = listOf(
            emailResult, passwordResult
        ).any { it.error != null }

        if (hasError) {
            _loginState.update {
                it.copy(
                    emailError = emailResult.error,
                    passwordError = passwordResult.error
                )
            }
            return
        }

        auth.signInWithEmailAndPassword(
            _loginState.value.email,
            _loginState.value.password
        ).addOnSuccessListener { result ->

            val user = auth.currentUser
            user?.let { firebaseUser ->

                _loginState.update {
                    it.copy(
                        emailError = null,
                        passwordError = null,
                        isLoggedIn = true
                    )
                }

                viewModelScope.launch {
                    loginValidationEventChannel.send(ValidationEvent.Success)
                }
            }
        }
            .addOnFailureListener { exception ->
                _loginState.update {
                    it.copy(
                        emailError = exception.message,
                        passwordError = exception.message
                    )
                }
            }
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
    }
}