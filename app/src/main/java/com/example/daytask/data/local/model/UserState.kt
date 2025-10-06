package com.example.daytask.data.local.model

import com.google.firebase.auth.FirebaseUser

data class UserState(
    val firebaseUser: FirebaseUser? = null
)
