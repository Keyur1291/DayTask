package com.example.daytask.domain

interface TaskEvent {

    data class OnTitleChange(val newTitle: String): TaskEvent
    data class OnDescriptionChange(val newDescription: String): TaskEvent
    data class OnTaskStatusChange(val newStatus: Boolean): TaskEvent
    data class OnTaskTimeChange(val newTime: String): TaskEvent
    data class OnTaskDueDateChange(val newDueDate: String): TaskEvent
}