package com.example.daytask.domain.use_cases

import com.example.daytask.domain.ValidationResult

class ValidateProjectTitle {

    fun execute(projectTitle: String): ValidationResult {

        if (projectTitle.isEmpty()) {
            return ValidationResult(
                success = false,
                error = "Title can't be empty"
            )
        }
        if (projectTitle.length < 2) {
            return ValidationResult(
                success = false,
                error = "Title must consist of at least 2 characters"
            )
        }
        return ValidationResult(
            success = true,
            error = null
        )
    }
}