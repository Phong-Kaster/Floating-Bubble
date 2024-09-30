package com.example.floatingbubble.ui.fragment.home


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.KeyEventDispatcher
import com.example.floatingbubble.R
import com.example.floatingbubble.enums.CloseBubbleBehavior
import com.example.floatingbubble.lifecycleobserver.NotificationLifecycleObserver
import com.example.floatingbubble.model.BubbleBuilder
import com.example.floatingbubble.service.FacebookBubbleService
import com.example.floatingbubble.ui.fragment.bubble.BubbleCompose
import com.example.floatingbubble.ui.theme.ColorBackground
import com.example.floatingbubble.ui.theme.customizedTextStyle
import com.example.floatingbubble.util.ServiceUtil.startBubbleService
import com.example.floatingbubble.util.ViewHelper
import com.example.jetpack.core.CoreFragment
import com.example.jetpack.core.CoreLayout
import com.example.jetpack.util.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : CoreFragment() {

    private lateinit var notificationLifecycleObserver: NotificationLifecycleObserver

    private val windowManager by lazy { requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNotificationLauncher()
        setupNotification()
        setupCloneBubble()
    }

    /*************************************************
     * setupNotificationLauncher
     */
    private fun setupNotificationLauncher() {
        notificationLifecycleObserver = NotificationLifecycleObserver(
            activity = requireActivity(),
            registry = requireActivity().activityResultRegistry
        )
        lifecycle.addObserver(notificationLifecycleObserver)
    }

    /*************************************************
     * setupNotification
     */
    private fun setupNotification() {
        // 1. Request POST NOTIFICATION permission if device has Android OS from 13
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val isAccessed: Boolean = PermissionUtil.isNotiEnabled(context = requireContext())
        if (isAccessed) return
        notificationLifecycleObserver.systemLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)


        // 2. Create notification channel and setup daily notification
        //NotificationManager.createNotificationChannel(context = requireContext())
        //NotificationManager.sendNotification(context = requireContext())

        // 3. Create lockscreen-styled notification and send it every day
        //LockscreenManager.createNotificationChannel(context = requireContext())
        //LockscreenManager.sendNotification(context = requireContext())
    }

    @SuppressLint("RestrictedApi")
    private fun setupCloneBubble(){
        val display = windowManager.defaultDisplay
        val screenSize = Point()
        display.getSize(screenSize)

        val imgView = ViewHelper.fromDrawable(requireContext(), R.drawable.ic_disclaimer, 40, 40)
        imgView.setOnClickListener {
            KeyEventDispatcher.dispatchBeforeHierarchy(
                imgView,
                KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
            )
        }

        val clone = BubbleBuilder(requireActivity())
            //.bubbleView(imgView)
            .bubbleCompose { BubbleCompose() }
            .forceDragging(false)
            .bubbleStyle(null)
            .startLocation((screenSize.x * 0F).toInt(), (screenSize.y * 0.5).toInt())    // in dp
            .startLocationPx((screenSize.x * 0F).toInt(), (screenSize.y * 0.5).toInt())  // in px
            .enableAnimateToEdge(true)
            .closeBubbleView(ViewHelper.fromDrawable(requireContext(), R.drawable.ic_close_bubble, 60, 60))
            .closeBubbleStyle(null)
            .closeBehavior(CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE)
            .distanceToClose(100)
            .bottomBackground(true)

        Log.d("HomeFragment", "setupMiniBubble - clone = ${clone}")
    }

    @Composable
    override fun ComposeView() {
        super.ComposeView()
        HomeLayout(
            onEnable = {
                requireActivity().startBubbleService()
            },
            onDisable = {
                val intent = Intent(requireActivity(), FacebookBubbleService::class.java)
                requireActivity().stopService(intent)
            }
        )
    }
}

@Composable
fun HomeLayout(
    onEnable: () -> Unit = {},
    onDisable: () -> Unit = {},
) {
    CoreLayout(
        backgroundColor = ColorBackground,
        bottomBar = { },
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.enable),
                    style = customizedTextStyle(
                        fontSize = 18,
                        fontWeight = 500,
                        color = Color.Black,
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(25.dp))
                        .clickable { onEnable() }
                        .background(color = Color.Cyan)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                )

                Text(
                    text = stringResource(id = R.string.disable),
                    style = customizedTextStyle(
                        fontSize = 18,
                        fontWeight = 500,
                        color = Color.Black,
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(25.dp))
                        .clickable { onDisable() }
                        .background(color = Color.Cyan)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewHomeLayout() {
    HomeLayout()
}