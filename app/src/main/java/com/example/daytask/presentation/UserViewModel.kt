package com.example.daytask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.data.local.model.UserState
import com.example.daytask.domain.UserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState())
    val userState = _userState.asStateFlow()

    fun onUserEvent(userEvent: UserEvent) {

        when(userEvent) {
            is UserEvent.UpdateUser -> {
                viewModelScope.launch {
                    _userState.update {
                        it.copy(
                            firebaseUser = userEvent.newUser
                        )
                    }
                }
            }
        }
    }
}