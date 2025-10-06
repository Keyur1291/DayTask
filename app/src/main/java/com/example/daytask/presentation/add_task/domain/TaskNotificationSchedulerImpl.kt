package com.example.daytask.presentation.add_task.domain

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.example.daytask.R
import com.example.daytask.data.local.model.Task
import com.example.daytask.presentation.MainActivity
import java.util.Calendar
import java.util.Random
import androidx.core.net.toUri

@RequiresApi(Build.VERSION_CODES.O)
class TaskNotificationSchedulerImpl @OptIn(ExperimentalMaterial3Api::class) constructor(
    private val context: Context,
    private val datePickerState: DatePickerState,
    private val timePickerState: TimePickerState
) : TaskNotificationScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun createPendingIntent(task: Task): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", task.title)
            putExtra("description", task.description)
            putExtra("time", task.time)
            putExtra("id", task.id)
            putExtra("projectId", task.projectId)
            putExtra("dueDate", task.dueDate)
            putExtra("isComplete", task.isComplete)
        }

        return PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.S)
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    override fun schedule(task: Task) {

        val selectedDate = Calendar.getInstance().apply {
            timeInMillis = datePickerState.selectedDateMillis ?: 0L
        }

        val year = selectedDate.get(Calendar.YEAR)
        val month = selectedDate.get(Calendar.MONTH)
        val day = selectedDate.get(Calendar.DAY_OF_MONTH)

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, timePickerState.hour, timePickerState.minute)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            createPendingIntent(task)
        )
    }

    override fun cancel(task: Task) {
        alarmManager.cancel {
            createPendingIntent(task)
        }
    }
}

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {

            val taskTile = intent?.getStringExtra("title") ?: "Task"
            val taskDescription = intent?.getStringExtra("description") ?: "Description"
            val taskTime = intent?.getStringExtra("time") ?: "00:00"
            val taskId = intent?.getIntExtra("id", 0) ?: 0
            val taskDueDate = intent?.getStringExtra("dueDate") ?: "00:00"
            val taskProjectId = intent?.getIntExtra("projectId", 0) ?: 0
            val taskIsComplete = intent?.getBooleanExtra("isComplete", false) == true

            val notificationManager =
                it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val runnerNotifier = RunnerNotifier(notificationManager, it)
            runnerNotifier.showNotification(
                Task(
                    id = taskId,
                    projectId = taskProjectId,
                    title = taskTile,
                    description = taskDescription,
                    time = taskTime,
                    dueDate = taskDueDate,
                    isComplete = taskIsComplete
                )
            )
        }
    }
}

class RunnerNotifier(
    notificationManager: NotificationManager,
    private val context: Context
) : Notifier(notificationManager) {

    override val notificationChannelId: String = "runner_channel_id"
    override val notificationChannelName: String = "Tasks"
    override val notificationId: Int = Random().nextInt(100)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun buildNotification(task: Task): Notification {

        val intent = Intent(
            Intent.ACTION_VIEW,
            "day-task://notification/$task".toUri()
        )
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addNextIntentWithParentStack(intent)
        val pendingIntent = taskStackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, notificationChannelId)
            .setStyle(
                NotificationCompat
                    .BigTextStyle()
                    .setBigContentTitle(getNotificationTitle(task))
                    .bigText(getNotificationMessage(task))
            ).setLargeIcon(context.bitmapFromResource(R.drawable.ic_logo))
            .setContentTitle(getNotificationTitle(task))
            .setContentText(getNotificationMessage(task))
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun getNotificationTitle(task: Task): String {
        return task.title
    }

    override fun getNotificationMessage(task: Task): String {
        return "Don't forget to ${task.description} before ${task.time} today!"
    }
}

private fun Context.bitmapFromResource(
    @DrawableRes resId: Int
) = BitmapFactory.decodeResource(
    resources,
    resId
)

abstract class Notifier(
    private val notificationManager: NotificationManager
) {

    abstract val notificationChannelId: String
    abstract val notificationChannelName: String
    abstract val notificationId: Int

    fun showNotification(task: Task) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createNotificationChannel()
            notificationManager.createNotificationChannel(channel)
        }
        val notification = buildNotification(task)
        notificationManager.notify(
            notificationId,
            notification
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    open fun createNotificationChannel(
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ): NotificationChannel {

        return NotificationChannel(
            notificationChannelId,
            notificationChannelName,
            importance
        )
    }

    abstract fun buildNotification(task: Task): Notification
    protected abstract fun getNotificationTitle(task: Task): String
    protected abstract fun getNotificationMessage(task: Task): String
}