package com.example.daytask.presentation.add_task.domain

import android.app.PendingIntent
import com.example.daytask.data.local.model.Task

interface TaskNotificationScheduler {

    fun createPendingIntent(task: Task): PendingIntent

    fun schedule(task: Task)

    fun cancel(task: Task)
}