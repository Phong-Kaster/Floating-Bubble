package com.example.floatingbubble.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import com.example.floatingbubble.R


class MyFloatingBubbleService : Service() {

    private val windowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private val TAG = this.javaClass.simpleName


    /** Inflater floating bubble layout */
    private val floatingBubbleLayout: View by lazy { LayoutInflater.from(this).inflate(R.layout.layout_floating_bubble, null) }
    private val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        super.onCreate()
        setupFloatingBubbleLayout()
        setupListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeViewImmediate(floatingBubbleLayout)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFloatingBubbleLayout() {
        val display = windowManager.defaultDisplay
        val screenSize = Point()
        display.getSize(screenSize)

        /** set floating icon in the middle on the right of screen*/
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.x = screenSize.x
        layoutParams.y = (screenSize.y * 0.5).toInt()

        windowManager.addView(floatingBubbleLayout, layoutParams)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListener() {
        val display = windowManager.defaultDisplay
        val screenSize = Point()
        display.getSize(screenSize)

        val listener = object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        //remember the initial position.
                        initialX = layoutParams.x
                        initialY = layoutParams.y

                        //get the touch location
                        initialTouchX = motionEvent.rawX
                        initialTouchY = motionEvent.rawY

                        return true
                    }
                    MotionEvent.ACTION_UP -> {}
                    MotionEvent.ACTION_MOVE -> {
                        //Calculate the X and Y coordinates of the view.
                        layoutParams.x = initialX + (motionEvent.rawX - initialTouchX).toInt()
                        layoutParams.y = initialY + (motionEvent.rawY - initialTouchY).toInt()

                        //Update the layout with new X & Y coordinate
                        windowManager.updateViewLayout(floatingBubbleLayout, layoutParams)
                        return true
                    }

                }
                return false
            }
        }
        try {
            floatingBubbleLayout.setOnTouchListener(listener)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}