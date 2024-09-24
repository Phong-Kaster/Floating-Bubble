package com.example.floatingbubble.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.floatingbubble.R
import com.example.floatingbubble.enums.AndroidVersions


class NotificationHelper @JvmOverloads constructor(
    private val context: Context,
    val channelId: String = "bubble_service",
    val channelName: String = "floating bubble",
    val notificationId: Int = 101,
) {

    /**
     * update notification if already exists
     * */
    @SuppressLint("MissingPermission")
    fun notify(notification: Notification) {
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    /**
     * create notification channel on android 8 and above
     * */
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, channelName)
        }
    }

    /**
     * Default notification for FloatingBubbleService foreground service.
     *
     * In case you don't have time :)
     * */
    fun defaultNotification(): Notification =
        NotificationCompat.Builder(context, channelId)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
            .setContentTitle(context.getString(R.string.floating_bubble_is_running))
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSilent(true)
            .build()


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
    ) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT   // IMPORTANCE_NONE recreate the notification if update
        ).apply {
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

}