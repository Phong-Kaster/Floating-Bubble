package com.example.floatingbubble.enums

/**
 * Close Bubble Behavior
 *
 * DYNAMIC_CLOSE_BUBBLE - close-bubble moving based on the bubble's location
 *
 * FIXED_CLOSE_BUBBLE (default) - bubble will automatically move to the close-bubble when it reaches the closable-area
 */
enum class CloseBubbleBehavior {
    DYNAMIC_CLOSE_BUBBLE,
    FIXED_CLOSE_BUBBLE
}