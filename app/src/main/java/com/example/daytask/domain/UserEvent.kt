package com.example.daytask.domain

import com.google.firebase.auth.FirebaseUser

interface UserEvent {

    data class UpdateUser(val newUser: FirebaseUser?): UserEvent
}