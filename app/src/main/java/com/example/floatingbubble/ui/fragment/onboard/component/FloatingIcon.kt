package com.example.floatingbubble.ui.fragment.onboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.floatingbubble.ui.theme.ColorTextHelp
import com.example.floatingbubble.ui.theme.customizedTextStyle
import com.example.floatingbubble.ui.theme.outerShadow
import com.example.floatingbubbleexample.R

@Composable
fun FloatingIcon(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .outerShadow(
                shadowRadius = 20.dp,
                spreadRadius = 2.dp,
                shadowColor = Color.Black,
                shape = RoundedCornerShape(15.dp)
            )
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_video_2),
            contentDescription = "Icon",
            modifier = Modifier
                .size(48.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = customizedTextStyle(
                    fontSize = 16,
                    fontWeight = 400,
                    color = Color.Black
                )
            )
            Text(
                text = stringResource(R.string.floating_icon),
                style = customizedTextStyle(
                    fontSize = 16,
                    fontWeight = 400,
                    color = ColorTextHelp
                )
            )
        }

        Switch(
            checked = false,
            onCheckedChange = {},
            enabled = false,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.White,
                checkedIconColor = Color.White,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.White
            )
        )
    }
}

@Preview
@Composable
private fun PreviewFloatingIcon() {
    Column(modifier = Modifier.fillMaxWidth()) {
        FloatingIcon()
    }
}