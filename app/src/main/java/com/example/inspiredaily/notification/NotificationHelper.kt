package com.example.inspiredaily.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.inspiredaily.R
import com.example.inspiredaily.ui.MainActivity
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    private val context : Context
) {

    fun sendNotification(title: String, message: String) {
        val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE ) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

            val channelId = "QUOTE_CHANNEL_ID"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Quote Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Shows daily quote notifications"
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)  // <-- important
                .setAutoCancel(true)
                .build()
            notificationManager.notify(1, notification)
        }

}