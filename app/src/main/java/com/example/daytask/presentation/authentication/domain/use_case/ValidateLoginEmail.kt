package com.example.daytask.presentation.authentication.domain.use_case

import com.example.daytask.domain.ValidationResult

class ValidateLoginEmail {

    fun execute(email: String): ValidationResult {
        if (email.isEmpty()) {
            return ValidationResult(
                success = false,
                error = "Email can't be empty"
            )
        }

        return ValidationResult(
            success = true,
            error = null
        )
    }
}