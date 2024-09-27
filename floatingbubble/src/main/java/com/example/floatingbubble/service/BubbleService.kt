package com.example.floatingbubble.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.floatingbubble.util.NotificationHelper
import com.example.floatingbubble.util.canDrawOverlays
import com.example.floatingbubble.util.sez

/**
 * Bubble Service is the parent class that sets up everything we need to use a service for drawing
 * a bubble overlay device' screens
 */
abstract class BubbleService : Service() {

    protected val TAG = this.javaClass.simpleName

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()
        checkIfApplicationCanDrawOverlay()
        createScreenEasy()
        startForegroundService()
        setupBubble()
    }

    override fun onDestroy() {
        removeAllBubble()
        super.onDestroy()
    }

    abstract fun setupBubble()
    abstract fun removeAllBubble()

    private fun checkIfApplicationCanDrawOverlay() {
        if (this.canDrawOverlays().not()) {
            throw SecurityException("Permission Denied: \"display over other app\" permission IS NOT granted!")
        }
    }

    private fun startForegroundService() {
        val notification = NotificationHelper(context = this)
        notification.createNotificationChannel()
        startForeground(notification.notificationId, notification.defaultNotification())
    }

    private fun createScreenEasy() {
        sez.with(this.applicationContext)
    }
}