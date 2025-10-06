package com.example.daytask.presentation.authentication.domain.use_case

import android.util.Patterns
import com.example.daytask.domain.ValidationResult

class ValidateRegisterEmail {

    fun execute(email: String): ValidationResult {
        if (email.isEmpty()) {
            return ValidationResult(
                success = false,
                error = "Email can't be empty"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                success = false,
                error = "Invalid email pattern"
            )
        }

        return ValidationResult(
            success = true,
            error = null
        )
    }
}