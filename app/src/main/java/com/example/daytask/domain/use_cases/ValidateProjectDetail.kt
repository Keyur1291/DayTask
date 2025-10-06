package com.example.daytask.domain.use_cases

import com.example.daytask.domain.ValidationResult

class ValidateProjectDetail {

    fun execute(projectDetail: String): ValidationResult {

        if (projectDetail.isEmpty()) {
            return ValidationResult(
                success = false,
                error = "Detail can't be empty"
            )
        }
        if (projectDetail.length < 2) {
            return ValidationResult(
                success = false,
                error = "Detail must consist of at least 2 characters"
            )
        }
        return ValidationResult(
            success = true,
            error = null
        )
    }
}