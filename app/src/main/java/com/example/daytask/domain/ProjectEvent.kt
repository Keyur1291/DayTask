package com.example.daytask.domain

import com.example.daytask.data.local.model.Project
import com.example.daytask.data.local.model.Task

interface ProjectEvent {

    data class UpsertProject(val project: Project): ProjectEvent
    data class DeleteProject(val project: Project): ProjectEvent

    data class UpsertTask(val task: Task, val projectId: Int): ProjectEvent
    data class DeleteTask(val task: Task): ProjectEvent

    data class GetProjectById(val projectId: Int): ProjectEvent

    data class SetProjectTitle(val newTitle :String): ProjectEvent
    data class SetProjectDescription(val newDescription :String): ProjectEvent
    data class SetProjectDueDate(val newDueDate :String): ProjectEvent
    data class SetProjectTasks(val newTasks : Task): ProjectEvent
    data class SetProjectProgress(val newProgress: Float): ProjectEvent
}