package com.example.daytask.domain

import com.example.daytask.data.local.model.*

data class ProjectState(
    val projects: List<ProjectWithTasks> = emptyList(),
    val id: Int = 0,
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val dueDate: String = "",
    val tasks: List<Task> = emptyList(),
    val progress: Float = 0f,
    val isLoading: Boolean = false,
    val error: String? = null
)