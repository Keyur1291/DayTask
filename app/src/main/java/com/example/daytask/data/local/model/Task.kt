package com.example.daytask.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    foreignKeys = [ForeignKey(
        entity = Project::class,
        parentColumns = ["id"],
        childColumns = ["projectId"]
    )]
)
@Serializable
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val projectId: Int,
    val title: String,
    val description: String,
    val time: String,
    val dueDate : String,
    var isComplete: Boolean
)
