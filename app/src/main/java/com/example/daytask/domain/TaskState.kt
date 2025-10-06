package com.example.daytask.domain

data class TaskState(
    val id: Int = 0,
    val projectId: Int = 0,
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val isCompleted: Boolean = false,
    val time: String = "",
    val dueDate: String = "",
)