package com.example.daytask.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.daytask.data.local.model.Project
import com.example.daytask.data.local.model.Task
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

@Database(
    exportSchema = false,
    entities = [Project::class, Task::class],
    version = 3
)
abstract class ProjectDatabase: RoomDatabase() {

    abstract val projectDao: ProjectDao
}