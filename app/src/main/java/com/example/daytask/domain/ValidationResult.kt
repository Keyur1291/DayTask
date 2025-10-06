package com.example.daytask.domain

data class ValidationResult(
    val success: Boolean = false,
    val error: String? = null
)