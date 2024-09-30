package com.example.floatingbubble.service

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.view.KeyEvent
import android.view.WindowManager
import androidx.core.view.KeyEventDispatcher
import com.example.floatingbubble.FloatingBubbleService
import com.example.floatingbubble.R
import com.example.floatingbubble.enums.CloseBubbleBehavior
import com.example.floatingbubble.model.BubbleBuilder
import com.example.floatingbubble.ui.fragment.bubble.BubbleCompose
import com.example.floatingbubble.util.ViewHelper


class FacebookBubbleService : FloatingBubbleService() {
    private val windowManager by lazy { getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    override fun onCreate() {
        super.onCreate()
        minimize()
    }


    @SuppressLint("RestrictedApi")
    override fun setupMiniBubble(): BubbleBuilder? {
        val imgView = ViewHelper.fromDrawable(this, R.drawable.ic_disclaimer, 40, 40)
        imgView.setOnClickListener {
            KeyEventDispatcher.dispatchBeforeHierarchy(
                imgView,
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
            )
        }

        val display = windowManager.defaultDisplay
        val screenSize = Point()
        display.getSize(screenSize)



        return BubbleBuilder(this@FacebookBubbleService)
            // set bubble view
            //.bubbleView(imgView)
            .triggerClickablePerimeterPx(5f)

            // or our sweetie, Jetpack Compose
            .bubbleCompose { BubbleCompose() }
            .forceDragging(true)

            // set style for the bubble, fade animation by default
            .bubbleStyle(null)

            // set start location for the bubble, (x=0, y=0) is the top-left
            .startLocation((screenSize.x * 0F).toInt(), (screenSize.y * 0.3).toInt())    // in dp
            .startLocationPx((screenSize.x * 0F).toInt(), (screenSize.y * 0.3).toInt())  // in px


            // enable auto animate bubble to the left/right side when release, true by default
            .enableAnimateToEdge(true)

            // set close-bubble view
            .closeBubbleView(ViewHelper.fromDrawable(this, R.drawable.ic_close_bubble, 60, 60))

            // set style for close-bubble, null by default
            .closeBubbleStyle(null)

            // DYNAMIC_CLOSE_BUBBLE: close-bubble moving based on the bubble's location
            // FIXED_CLOSE_BUBBLE (default): bubble will automatically move to the close-bubble when it reaches the closable-area
            .closeBehavior(CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE)

            // the more value (dp), the larger closeable-area
            .distanceToClose(100)

            // enable bottom background, false by default
            .bottomBackground(true)
    }

}