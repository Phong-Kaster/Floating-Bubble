package com.example.floatingbubble.lifecycleobserver

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject


class OverlayPermissionLifecycleObserver
@Inject
constructor(
    private val registry: ActivityResultRegistry,
    private val activity: Activity,
    private val callback: Callback
) : DefaultLifecycleObserver {

    lateinit var settingLauncher: ActivityResultLauncher<Intent>

    private val tag = "OverlayPermission"
    override fun onCreate(owner: LifecycleOwner) {
        settingLauncher = createSystemLauncher(owner)
    }

    fun requestOverlayPermission() {
        if (Settings.canDrawOverlays(activity)) {
            callback.gotoNextScreen()
            return
        }

        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + activity.packageName)
        )
        settingLauncher.launch(intent)
    }


    /**
     * To open app setting*/
    private fun createSystemLauncher(owner: LifecycleOwner): ActivityResultLauncher<Intent> {
        return registry.register(
            "OverlaySettingLauncher",
            owner,
            ActivityResultContracts.StartActivityForResult()
        ) {

        }
    }

    interface Callback {
        fun gotoNextScreen()
    }
}