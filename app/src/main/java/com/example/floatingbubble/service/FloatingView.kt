package com.example.floatingbubble.service

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator

class FloatView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val params: WindowManager.LayoutParams = WindowManager.LayoutParams()
    private val metrics: DisplayMetrics = DisplayMetrics()
    private var x = 0f
    private var y = 0f
    private var touchX = 0f
    private var touchY = 0f

    init {
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        x = event.rawX
        y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
            }
            MotionEvent.ACTION_MOVE -> updateViewPosition()
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                animateToEdge()
                touchX = 0f
                touchY = 0f
            }
        }
        return super.onTouchEvent(event)
    }

    private fun animateToEdge() {
        val currentX = (x - touchX).toInt()
        val ani: ValueAnimator = if (currentX > metrics.widthPixels / 2) {
            ValueAnimator.ofInt(currentX, metrics.widthPixels - measuredWidth)
        } else {
            ValueAnimator.ofInt(currentX, 0)
        }
        params.y = (y - touchY).toInt().coerceIn(0, metrics.heightPixels - measuredHeight)
        ani.addUpdateListener { animation ->
            params.x = animation.animatedValue as Int
            wm.updateViewLayout(this@FloatView, params)
        }
        ani.duration = 200L
        ani.interpolator = AccelerateDecelerateInterpolator()
        ani.start()
    }

    private fun updateViewPosition() {
        params.x = (x - touchX).toInt().coerceIn(0, metrics.widthPixels - measuredWidth)
        params.y = (y - touchY).toInt().coerceIn(0, metrics.heightPixels - measuredHeight)
        wm.updateViewLayout(this, params)
    }

    fun getWindowManagerLayoutParams(): WindowManager.LayoutParams {
        return params
    }
}
