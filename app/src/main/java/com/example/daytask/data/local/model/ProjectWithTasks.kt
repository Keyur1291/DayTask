package com.example.daytask.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Serializable
data class ProjectWithTasks(
    @Embedded val project: Project = Project(),
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val tasks: List<Task> = emptyList()
)