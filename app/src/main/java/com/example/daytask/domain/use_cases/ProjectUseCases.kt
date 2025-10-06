package com.example.daytask.domain.use_cases

data class ProjectUseCases(
    val validateProjectTitle: ValidateProjectTitle = ValidateProjectTitle(),
    val validateProjectDetail: ValidateProjectDetail = ValidateProjectDetail(),
)