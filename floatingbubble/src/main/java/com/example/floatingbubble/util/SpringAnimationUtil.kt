package com.example.floatingbubble.util

import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

internal object SpringAnimationUtil {

    /**
     * Create simple spring animation
     *
     * @param startValue Initial value of the animation.
     * @param finalPosition Target value of the animation.
     * @param event An instance of the Event interface to handle animation events.
     * @param stiffness Stiffness of the spring (default is SpringForce.STIFFNESS_LOW).
     * @param dampingRatio Damping ratio of the spring (default is SpringForce.DAMPING_RATIO_LOW_BOUNCY)
     */
    fun startSpringX(
        startValue: Float,
        finalPosition: Float,
        event: Event,
        stiffness: Float = SpringForce.STIFFNESS_LOW,
        dampingRatio: Float = SpringForce.DAMPING_RATIO_LOW_BOUNCY,
    ): SpringAnimation {
        /*1. Define*/
        val animation = SpringAnimation(FloatValueHolder())
        animation.setStartValue(startValue)

        /*2. Establish animation attribute */
        animation.spring = SpringForce()
        animation.spring.setFinalPosition(finalPosition)
        animation.spring.stiffness = stiffness
        animation.spring.dampingRatio = dampingRatio

        /*3. Establish listener */
        animation.addUpdateListener { anim, value, velocity ->
            event.onUpdate(value)
        }
        animation.addEndListener { anim, canceled, value, velocity ->
            event.onEnd()
        }

        event.onStart()
        animation.start()

        return animation
    }

    /**
     * Creates a spring animation that follows a path from a start point to an end point
     *
     * @param startX Starting X coordinates of the animation
     * @param startY Starting Y coordinates of the animation
     * @param endX Ending X coordinates of the animation
     * @param endY Ending Y coordinates of the animation
     * @param event An instance of the Event interface to handle animation events.
     * @param stiffness Stiffness of the spring (default is SpringForce.STIFFNESS_MEDIUM).
     * @param dampingRatio Damping ratio of the spring (default is SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY).
     */
    fun animateSpringPath(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        event: Event,
        stiffness: Float = SpringForce.STIFFNESS_MEDIUM,
        dampingRatio: Float = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
    ): SpringAnimation {
        /*1. Establish animation attribute*/
        val animation = SpringAnimation(FloatValueHolder())

        val springForce = SpringForce()
        animation.spring.stiffness = stiffness
        animation.spring.dampingRatio = dampingRatio

        /*2. Specify final position of the animation*/
        val xDistance = endX - startX
        val yDistance = endY - startY

        if (yDistance > xDistance) {
            animation.setStartValue(startY)
            springForce.finalPosition = endY

            animation.addUpdateListener { anim, value, velocity ->
                val ratio = 1 - (endY - value) / yDistance
                event.onUpdatePoint(
                    x = startX + xDistance * ratio,
                    y = value
                )
            }
        } else {
            animation.setStartValue(startX)
            springForce.finalPosition = endX

            animation.addUpdateListener { anim, value, velocity ->
                val ratio = (value - startX) / xDistance
                event.onUpdatePoint(
                    x = value,
                    y = startY + yDistance * ratio
                )
            }
        }


        /*3. Set spring force */
        animation.spring = springForce
        animation.addEndListener { anim, canceled, value, velocity ->
            event.onEnd()
        }

        event.onStart()
        animation.start()
        return animation
    }

    interface Event {
        fun onStart() {}
        fun onEnd() {}
        fun onUpdate(float: Float) {}
        fun onUpdatePoint(x: Float, y: Float) {}
    }
}