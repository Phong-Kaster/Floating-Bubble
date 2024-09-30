package com.example.floatingbubble.ui.fragment.onboard

import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.floatingbubble.lifecycleobserver.OverlayPermissionLifecycleObserver
import com.example.floatingbubble.ui.fragment.onboard.component.FloatingIcon
import com.example.floatingbubble.ui.theme.ColorPrimary
import com.example.floatingbubble.ui.theme.customizedTextStyle
import com.example.floatingbubbleexample.R
import com.example.jetpack.core.CoreFragment
import com.example.jetpack.core.CoreLayout
import com.example.jetpack.util.NavigationUtil.safeNavigate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnboardPermissionFragment : CoreFragment() {

    private lateinit var overlayLifecycleObserver: OverlayPermissionLifecycleObserver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gotoNextScreenIfPermissionGranted()
        setupOverlayLauncher()
    }

    private fun gotoNextScreenIfPermissionGranted() {
        if (Settings.canDrawOverlays(activity)) {
            safeNavigate(destination = R.id.toHome)
            return
        }
    }

    /*************************************************
     * setupOverlayLauncher
     */
    private val callback = object : OverlayPermissionLifecycleObserver.Callback {
        override fun gotoNextScreen() {
            safeNavigate(destination = R.id.toHome)
        }
    }

    private fun setupOverlayLauncher() {
        overlayLifecycleObserver = OverlayPermissionLifecycleObserver(
            activity = requireActivity(),
            registry = requireActivity().activityResultRegistry,
            callback = callback
        )
        lifecycle.addObserver(overlayLifecycleObserver)
    }


    @Composable
    override fun ComposeView() {
        super.ComposeView()
        OnboardPermissionLayout(
            onSkip = {
                safeNavigate(destination = R.id.toHome)
            },
            onEnable = {
                overlayLifecycleObserver.requestOverlayPermission()
            }
        )
    }
}


@Composable
fun OnboardPermissionLayout(
    onSkip: () -> Unit = {},
    onEnable: () -> Unit = {},
) {
    val list = listOf(
        R.string.quick_record_control,
        R.string.rapid_screen_capture,
        R.string.explore_brush_and_facecam_options,
        R.string.enhance_recording_stability,
    )

    CoreLayout(
        bottomBar = {

        },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_onboard_2),
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.7f),
                    )

                    FloatingIcon(
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }

                Text(
                    text = stringResource(R.string.grant_permission_to_enable),
                    style = customizedTextStyle(
                        fontSize = 16,
                        fontWeight = 500,
                    ),
                    textAlign = TextAlign.Center,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(
                            color = Color(0xFFF9F9F9),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(16.dp)
                ) {
                    list.forEach { text ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Icon",
                                tint = Color(0xFF08C274),
                            )

                            Text(
                                text = stringResource(text),
                                color = Color(0xFF333333),
                                style = customizedTextStyle(fontSize = 16, fontWeight = 400),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.skip),
                        style = customizedTextStyle(
                            fontSize = 18, fontWeight = 400,
                            color = Color.Black
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .clickable { onSkip() }
                            .border(
                                width = 0.5.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.enable),
                        style = customizedTextStyle(
                            fontSize = 18, fontWeight = 400,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .clickable { onEnable() }
                            .border(
                                width = 1.dp,
                                color = Color(0xFFFF7E62),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(color = ColorPrimary)
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewOnboardPermissionLayout() {
    OnboardPermissionLayout()
}