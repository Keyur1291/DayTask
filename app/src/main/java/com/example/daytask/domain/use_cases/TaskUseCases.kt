package com.example.daytask.domain.use_cases

data class TaskUseCases(
    val validateTaskTitle: ValidateTaskTitle = ValidateTaskTitle(),
    val validateTaskDetail: ValidateTaskDetail = ValidateTaskDetail(),
)