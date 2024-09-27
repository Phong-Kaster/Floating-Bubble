package com.example.floatingbubble

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.example.floatingbubble.model.Bubble
import com.example.floatingbubble.ui.BubbleLinearLayout
import com.example.floatingbubble.util.SpringAnimationUtil
import com.example.floatingbubble.util.afterMeasured
import com.example.floatingbubble.util.getXYOnScreen
import com.example.floatingbubble.util.sez
import com.example.floatingbubble.util.updateGestureExclusion
import kotlin.math.abs

class FloatingBubble(
    context: Context,
    containCompose: Boolean = false,
    onDispatchKeyEvent: ((KeyEvent) -> Boolean?)? = null,
    private val forceDragging: Boolean = false,
    private val triggerClickableAreaPx: Float = 1f,
) : Bubble(
    context = context,
    root = LayoutInflater.from(context).inflate(R.layout.layout_bubble, null).apply {
        if (onDispatchKeyEvent != null) {
            (this as BubbleLinearLayout).setOnDispatchKeyEvent(onDispatchKeyEvent)
        }
    },
    containCompose = containCompose
) {
    private val TAG = this.javaClass.simpleName

    /**
     * store previous point for later usage, reset after finger down
     * */
    private val prevPoint = Point(0, 0) // the initial position of the finger when it is pressed
    private val rawPointOnDown = PointF(0f, 0f) //
    private val newPoint = Point(0, 0)
    private var halfScreenWidth = sez.fullWidth / 2


    private var springAnimation: SpringAnimation? = null

    internal var callback: FloatingBubbleCallback? = null
    internal var enableDrag: Boolean = false
    private var ignoreClick: Boolean = false

    init {
        customTouch()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun customTouch() {

        fun handleMovement(event: MotionEvent) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    prevPoint.x = layoutParams.x
                    prevPoint.y = layoutParams.y

                    rawPointOnDown.x = event.rawX
                    rawPointOnDown.y = event.rawY

                    callback?.onFingerDown(event.rawX, event.rawY)
                }

                MotionEvent.ACTION_MOVE -> {
                    if (!enableDrag) return
                    callback?.onFingerMove(event.rawX, event.rawY)
                }

                MotionEvent.ACTION_UP -> {
                    callback?.onFingerUp(event.rawX, event.rawY)
                }
            }
        }


        fun ignoreChildClickEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> ignoreClick = false
                MotionEvent.ACTION_UP -> ignoreClick = false
                MotionEvent.ACTION_MOVE -> {
                    if (abs(event.rawX - rawPointOnDown.x) > triggerClickableAreaPx ||
                        abs(event.rawY - rawPointOnDown.y) > triggerClickableAreaPx
                    ) {
                        ignoreClick = true
                    }
                }
            }

            return ignoreClick
        }


        (root as BubbleLinearLayout).apply {
            afterMeasured { updateGestureExclusion() }

            if (forceDragging) {
                // maybe these are the reasons why the compose view stop, really weird. compose view not show in some case
                doOnTouchEvent = { handleMovement(it) }
                ignoreChildEvent = { motionEvent -> ignoreChildClickEvent(motionEvent) }
            } else {
                this.setOnTouchListener { view, motionEvent ->
                    handleMovement(motionEvent)
                    true
                }
            }
        }
    }

    // spring animation --------------------------------------------------------------------------------
    fun safeCancelAnimation() {
        springAnimation?.cancel()
    }

    fun animateToEdge() {
        springAnimation?.cancel()
        springAnimation = null

        val bubbleWidth = root.width // -1

        val iconX = root.getXYOnScreen().first

        val isOnTheLeftSide = iconX + bubbleWidth / 2 < halfScreenWidth
        val startX: Int
        val endX: Int
        if (isOnTheLeftSide) {
            startX = iconX
            endX = 0
        } else {
            startX = iconX
            endX = sez.safeWidth - bubbleWidth
        }

        springAnimation = SpringAnimationUtil.startSpringX(
            startValue = startX.toFloat(),
            finalPosition = endX.toFloat(),
            event = object : SpringAnimationUtil.Event {
                override fun onUpdate(float: Float) {
                    try {
                        layoutParams.x = float.toInt()
                        update()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d(TAG, "onUpdate with exception ${e.message}")
                    }
                }

                override fun onEnd() {
                    springAnimation = null
                }
            }
        )
    }

    fun animateTo(x: Float, y: Float, stiffness: Float = SpringForce.STIFFNESS_MEDIUM) {
        SpringAnimationUtil.animateSpringPath(
            startX = newPoint.x.toFloat(),
            startY = newPoint.y.toFloat(),
            endX = x,
            endY = y,
            event = object : SpringAnimationUtil.Event {
                override fun onUpdatePoint(x: Float, y: Float) {
                    layoutParams.x = x.toInt()
                    layoutParams.y = y.toInt()
                    update()
                }
            },
            stiffness = stiffness,
        )
    }

    // bubble position --------------------------------------------------------------------------------
    fun updateLocationUI(x: Float, y: Float) {
        val mIconDeltaX = x - rawPointOnDown.x
        val mIconDeltaY = y - rawPointOnDown.y

        newPoint.x = prevPoint.x + mIconDeltaX.toInt()
        newPoint.y = prevPoint.y + mIconDeltaY.toInt()

        //region prevent bubble Y point move outside the screen
        val safeTopY = 0
        val safeBottomY = sez.safeHeight - root.height

        val isAboveStatusBar = newPoint.y < safeTopY
        val isUnderSoftNavBar = newPoint.y > safeBottomY
        if (isAboveStatusBar) {
            newPoint.y = safeTopY
        } else if (isUnderSoftNavBar) {
            newPoint.y = safeBottomY
        }
        //endregion

        layoutParams.x = newPoint.x
        layoutParams.y = newPoint.y

        update()
    }

    /**
     * set location without updating UI
     * */
    fun setLocation(x: Float, y: Float) {
        newPoint.x = x.toInt()
        newPoint.y = y.toInt()
    }

    /**
     * without status bar
     * */
    fun rawLocationOnScreen(): Pair<Float, Float> {
        return Pair(newPoint.x.toFloat(), newPoint.y.toFloat())
    }
}