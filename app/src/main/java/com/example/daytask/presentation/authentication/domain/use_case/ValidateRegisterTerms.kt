package com.example.daytask.presentation.authentication.domain.use_case

import com.example.daytask.domain.ValidationResult

class ValidateRegisterTerms {

    fun execute(isChecked: Boolean): ValidationResult {

        return if(isChecked) {
            ValidationResult(
                success = true,
                error = null
            )
        } else {
            ValidationResult(
                success = false,
                error = "Please check the terms & conditions"
            )
        }
    }
}