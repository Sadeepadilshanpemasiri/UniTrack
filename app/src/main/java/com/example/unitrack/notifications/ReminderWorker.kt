package com.example.unitrack.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.unitrack.R

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    // Get the title from the input data passed to the worker
    private val title = inputData.getString(KEY_TITLE) ?: "Reminder"
    private val channelId = "UniTrackRemindersChannel"
    private val notificationId = id.hashCode() // Unique ID for each notification

    override fun doWork(): Result {
        // 1. Create a Notification Channel (required for Android 8.0+)
        createNotificationChannel()

        // 2. Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your app's icon
            .setContentTitle("Upcoming Reminder: $title")
            .setContentText("Your deadline is tomorrow! Don't forget.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // Dismiss the notification when clicked
            .build()

        // 3. Show the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "UniTrack Reminders"
            val descriptionText = "Channel for UniTrack reminders and deadlines"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val KEY_TITLE = "reminder_title"
    }
}
