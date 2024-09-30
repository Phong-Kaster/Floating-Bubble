package com.example.floatingbubble.ui.fragment.bubble

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.floatingbubbleexample.R


@Composable
fun BubbleCompose() {

    //val expanded = true
    var expanded by remember { mutableStateOf(true) }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        IconButton(
            onClick = { expanded = !expanded },
            content = {
                Image(
                    painter = painterResource(id = R.drawable.ic_camcorder),
                    contentDescription = "Icon",
                    modifier = Modifier.size(48.dp)
                )
            }
        )

        if (expanded) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 5.dp, alignment = Alignment.CenterHorizontally),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.White)
            ) {
                IconButton(
                    onClick = { },
                    content = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_record),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                )

                IconButton(
                    onClick = { },
                    content = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                )

                IconButton(
                    onClick = { },
                    content = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBubble() {
    BubbleCompose(

    )
}