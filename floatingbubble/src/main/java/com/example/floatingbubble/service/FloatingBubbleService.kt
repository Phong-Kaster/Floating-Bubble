package com.example.floatingbubble.service

import android.content.res.Configuration
import com.example.floatingbubble.enums.BubbleState
import com.example.floatingbubble.model.FloatingBubble

class FloatingBubbleService : BubbleService() {

    /** the current state of floating bubble*/
    private var state = BubbleState.NONE

    var miniBubble: FloatingBubble? = null

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }

    /**
     * get ready for users configuration
     */
    override fun setupBubble() {

    }

    /**
     * remove & destroy bubble is drawing overlay screens & service is running
     */
    override fun removeAllBubble() {
        state = BubbleState.NONE
    }

    fun expand() {
        state = BubbleState.EXPANDED_BUBBLE
    }

    fun minimize() {
        state = BubbleState.MINI_BUBBLE
    }
}