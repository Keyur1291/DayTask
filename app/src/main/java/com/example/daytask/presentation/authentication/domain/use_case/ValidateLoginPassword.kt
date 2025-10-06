package com.example.daytask.presentation.authentication.domain.use_case

import com.example.daytask.domain.ValidationResult

class ValidateLoginPassword {

    fun execute(password: String): ValidationResult {

        if (password.isEmpty()) {
            return ValidationResult(
                success = false,
                error = "Password can't be empty"
            )
        }

        return ValidationResult(
            success = true,
            error = null
        )
    }
}