package com.example.daytask.domain.use_cases

import com.example.daytask.domain.ValidationResult

class ValidateTaskTitle {

    fun execute(taskTitle: String): ValidationResult {

        if (taskTitle.isEmpty()) {
            return ValidationResult(
                success = false,
                error = "Title can't be empty"
            )
        }
        if (taskTitle.length < 2) {
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