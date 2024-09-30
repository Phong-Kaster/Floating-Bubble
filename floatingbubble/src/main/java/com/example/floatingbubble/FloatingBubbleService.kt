package com.example.floatingbubble

import android.content.res.Configuration
import android.graphics.PointF
import android.util.Log
import com.example.floatingbubble.enums.BubbleState
import com.example.floatingbubble.enums.CloseBubbleBehavior
import com.example.floatingbubble.model.BubbleBuilder
import com.example.floatingbubble.service.BubbleService
import com.example.floatingbubble.util.sez
import kotlin.math.abs

abstract class FloatingBubbleService : BubbleService() {

    /** the current state of floating miniBubble*/
    private var state = BubbleState.NONE

    var miniBubble: FloatingBubble? = null
        private set

    abstract fun setupMiniBubble(): BubbleBuilder?


    //region public methods
    var latestX = 0f
    var latestY = 0f

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        miniBubble?.remove()
        sez.refresh()
        createMiniBubble()
        when (state) {
            BubbleState.MINI_BUBBLE -> minimize()
            BubbleState.EXPANDED_BUBBLE -> expand()
            else -> {}
        }
    }

    /**
     * get ready for users configuration
     */
    override fun setupBubble() {
        Log.d(TAG, "setupBubble")
        createMiniBubble()
    }

    private fun createMiniBubble() {
        Log.d(TAG, "createMiniBubble")
        val miniBubbleBuilder: BubbleBuilder? = setupMiniBubble()


        Log.d(TAG, "createMiniBubble - miniBubbleBuilder = ${miniBubbleBuilder == null} ")
        if (miniBubbleBuilder == null) {
            Log.d(TAG, "miniBubbleBuilder is null !")
            return
        }

        // setup miniBubble ------------------------------------------------------------------------
        miniBubble = FloatingBubble(
            context = this,
            forceDragging = miniBubbleBuilder.forceDragging,
            containCompose = miniBubbleBuilder.bubbleCompose != null,
            triggerClickableAreaPx = miniBubbleBuilder.triggerClickablePerimeterPx,
            onDispatchKeyEvent = null
        )

        if (miniBubbleBuilder.bubbleView != null) {
            miniBubble!!.rootGroup.addView(miniBubbleBuilder.bubbleView)
        } else {
            miniBubble!!.rootGroup.addView(miniBubbleBuilder.bubbleCompose)
        }

        miniBubble!!.callback = CustomBubbleListener(
            floatingBubble = miniBubble!!,
            enableAnimateToEdge = miniBubbleBuilder.isAnimateToEdgeEnabled,
            closeBehavior = miniBubbleBuilder.behavior,
            isCloseBubbleEnabled = true,
            triggerClickableAreaPx = miniBubbleBuilder.triggerClickablePerimeterPx
        )
        miniBubble!!.layoutParams = miniBubbleBuilder.defaultLayoutParams()
        miniBubble!!.enableDrag = miniBubbleBuilder.isBubbleDraggable

        // setup close-miniBubble ------------------------------------------------------------------
//            if (bubbleBuilder.closeView != null) {
//                closeBubble = FloatingCloseBubble(
//                    context = context,
//                    root = bubbleBuilder.closeView!!,
//                    distanceToClosePx = bubbleBuilder.distanceToClosePx,
//                    bottomPaddingPx = bubbleBuilder.closeBubbleBottomPaddingPx
//                )
//                closeBubble!!.layoutParams.apply {
//                    bubbleBuilder.closeBubbleStyle?.let {
//                        windowAnimations = it
//                    }
//                }
//            }

        // setup bottom-background
//            if (bubbleBuilder.isBottomBackgroundEnabled) {
//                bottomBackground = FloatingBottomBackground(context)
//            }
    }

    /**
     * remove & destroy miniBubble is drawing overlay screens & service is running
     */
    override fun removeAllBubble() {
        state = BubbleState.NONE
        miniBubble?.remove()
    }

    fun expand() {
        state = BubbleState.EXPANDED_BUBBLE
        try {
            miniBubble?.remove()
        }catch (ex: Exception) {
            ex.printStackTrace()
            Log.d(TAG, "expand with exception ${ex.message}")
        }

    }

    fun minimize() {
        state = BubbleState.MINI_BUBBLE
        try {
            miniBubble?.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d(TAG, "minimize has exception ${ex.message}")
        }

    }

    private inner class CustomBubbleListener(
        private val floatingBubble: FloatingBubble,
        private val enableAnimateToEdge: Boolean,
        private val closeBehavior: CloseBubbleBehavior = CloseBubbleBehavior.FIXED_CLOSE_BUBBLE,
        private val isCloseBubbleEnabled: Boolean = true,
        private val triggerClickableAreaPx: Float = 1f
    ) : FloatingBubbleCallback {

        private var isCloseBubbleVisible = false
        private var onDownLocation = PointF(0f, 0f)

        override fun onFingerDown(x: Float, y: Float) {
            floatingBubble.safeCancelAnimation()
            onDownLocation = PointF(x, y)


            latestX = x
            latestY = y
        }

        override fun onFingerUp(x: Float, y: Float) {
            isCloseBubbleVisible = false
            //tryRemoveCloseBubbleAndBackground()

//            var shouldDestroy =
//                when (closeBehavior) {
//
//                    CloseBubbleBehavior.FIXED_CLOSE_BUBBLE -> {
//                        closeBubble?.isFingerInsideClosableArea(x, y) ?: false
//                    }
//
//                    CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE -> {
//                        closeBubble?.isBubbleInsideClosableArea(floatingBubble)
//                            ?: false
//                    }
//                }
//            if (closeBubble?.ableToInteract == false) {
//                shouldDestroy = false
//            }

//            if (shouldDestroy) {
//                serviceInteractor?.requestStop()
//            } else {
//                if (mAnimateToEdge) {
//                    floatingBubble.animateToEdge()
//                }
//            }

            if (enableAnimateToEdge) {
                floatingBubble.animateToEdge()
            }

            latestX = x
            latestY = y
        }

        override fun onFingerMove(x: Float, y: Float) {
            when (closeBehavior) {
                CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE -> {
                    floatingBubble.updateLocationUI(x, y)
                    val (mx, my) = floatingBubble.rawLocationOnScreen()
                    //closeBubble?.followBubble(mx.toInt(), my.toInt(), floatingBubble)
                }

                CloseBubbleBehavior.FIXED_CLOSE_BUBBLE -> {
//                    val isAttracted = closeBubble?.tryAttractBubble(floatingBubble, x, y) ?: false
//                    if (isAttracted.not()) {
//                        floatingBubble.updateLocationUI(x, y)
//                        expandedBubble?.updateLocationUI(x, y)
//                    }
                }
            }
            if (isCloseBubbleEnabled && isCloseBubbleVisible.not()) {
                // TODO: check if close-bubble should be shown
                if (abs(onDownLocation.x - x) > triggerClickableAreaPx || abs(onDownLocation.y - y) > triggerClickableAreaPx) {
                    //tryShowCloseBubbleAndBackground()
                    isCloseBubbleVisible = true
                }
            }
            latestX = x
            latestY = y
        }
    }
}