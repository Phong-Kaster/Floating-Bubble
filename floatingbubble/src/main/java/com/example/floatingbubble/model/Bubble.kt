package com.example.floatingbubble.model

import android.app.Service
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.floatingbubble.ui.ComposeLifecycleOwner

open class Bubble
constructor(
    context: Context,
    root: View?,
    private val containCompose: Boolean = false
) {
    private val TAG = this.javaClass.simpleName

    /* Window Manager */
    private var _windowManager: WindowManager? = null
    private val windowManager: WindowManager
        get() = _windowManager!!


    /* Params */
    private var _layoutParams: WindowManager.LayoutParams
    var layoutParams
        get() = _layoutParams
        set(value) {
            _layoutParams = value
        }

    /*  Root View */
    private var _root: View? = null
    var root
        get() = _root!!
        set(value) {
            _root = value
        }
    val rootGroup: ViewGroup
        get() = root as ViewGroup

    /* Compose-based UI*/
    private var composeOwner: ComposeLifecycleOwner? = null
    private var isComposeOwnerInitialized: Boolean = false

    init {
        _windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        _layoutParams = WindowManager.LayoutParams()
        _root = root

        if (containCompose) {
            composeOwner = ComposeLifecycleOwner()
            composeOwner?.attachToDecorView(root)
        }
    }


    open fun show() {
        try {
            if (containCompose) {

                if (isComposeOwnerInitialized.not()) {
                    composeOwner!!.onCreate() // only call this once
                    isComposeOwnerInitialized = true

                }
                composeOwner?.onStart()
                composeOwner?.onResume()
            }

            windowManager.addView(root, _layoutParams)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * - don't call remove if the view did not call show() previously
     * - call windowManager.removeViewImmediate() will make the view can't change when added again
     * - add this line 'if (root.windowToken == null) return' will prevent the view from being removed in some cases
     * */
    open fun remove() {
        //if (root.windowToken == null) return
        try {
            windowManager.removeView(root)

            if (containCompose) {
                composeOwner?.onPause()
                composeOwner?.onStop()
                composeOwner?.onDestroy()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d(TAG, "remove has exception ${ex.message}")
        }
    }

    fun update() {
        //if (root.windowToken == null) return
        try {
            windowManager.updateViewLayout(root, _layoutParams)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d(TAG, "update has exception ${ex.message}")
        }
    }
}