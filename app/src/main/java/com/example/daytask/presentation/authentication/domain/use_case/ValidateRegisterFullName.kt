package com.example.daytask.presentation.authentication.domain.use_case

import com.example.daytask.domain.ValidationResult

class ValidateRegisterFullName {

    fun execute(fullName: String): ValidationResult {

        if (fullName.isEmpty()) {
            return ValidationResult(
                success = false,
                error = "Full name can't be empty"
            )
        }
        if (fullName.length < 2) {
            return ValidationResult(
                success = false,
                error = "Full name must consist of at least 2 characters"
            )
        }
        return ValidationResult(
            success = true,
            error = null
        )
    }
}