package com.example.floatingbubble.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.example.floatingbubble.service.MyFloatingBubbleService

object ServiceUtil {

    fun Activity.isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = activityManager.getRunningServices(Int.MAX_VALUE)

        for (service in services) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    fun Activity.startBubbleService() {
        if (!isServiceRunning(MyFloatingBubbleService::class.java) && Settings.canDrawOverlays(this)) {
            val intent = Intent(this, MyFloatingBubbleService::class.java)
            application.startService(intent)
        }
    }
}