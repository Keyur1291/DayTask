package com.example.daytask.presentation.authentication.domain.use_case

import com.example.daytask.domain.ValidationResult

class ValidateRegisterPassword {

    fun execute(password: String): ValidationResult {

        val containsLettersAndDigits =
            password.any { it.isDigit() } && password.any { it.isLetter() }
        if (password.isEmpty()) {
            return ValidationResult(
                success = false,
                error = "Password can't be empty"
            )
        }
        if (password.length < 8) {
            return ValidationResult(
                success = false,
                error = "Password must consist of at least 8 characters"
            )
        }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                success = false,
                error = "Password must contain at least one special character, digit and one letter"
            )
        }
        return ValidationResult(
            success = true,
            error = null
        )
    }
}