package com.example.floatingbubble

interface FloatingBubbleCallback {
    /** the location of the finger when users press the finger
     * */
    fun onFingerDown(x: Float, y: Float)

    /** the location of the finger when users release the finger
     * */
    fun onFingerUp(x: Float, y: Float)

    /** the location of the finger when move on the screen
     * */
    fun onFingerMove(x: Float, y: Float)
}