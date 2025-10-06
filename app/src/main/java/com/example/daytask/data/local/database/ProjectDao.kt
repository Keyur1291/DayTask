package com.example.daytask.data.local.database

import androidx.room.*
import com.example.daytask.data.local.model.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProjectDao {

    @Upsert
    abstract suspend fun upsertProject(project: Project)

    @Delete
    abstract suspend fun deleteProject(project: Project)

    @Upsert
    abstract suspend fun upsertTask(task: Task)

    @Delete
    abstract suspend fun deleteTask(task: Task)

    @Transaction
    @Query("SELECT * FROM project ORDER BY title ASC")
    abstract fun getProjects(): Flow<List<ProjectWithTasks>>

    @Transaction
    @Query("SELECT * FROM project WHERE id = :projectId")
    abstract fun getProjectById(projectId: Int): ProjectWithTasks
}