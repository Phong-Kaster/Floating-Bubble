package com.example.floatingbubble.ui.fragment.bubble

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.floatingbubbleexample.R


@Composable
fun BubbleCompose() {

    //var expanded by remember { mutableStateOf(true) }
    var expanded = true

//    Box(modifier = Modifier.size(48.dp)) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_video_2),
//            contentDescription = "Icon",
//            modifier = Modifier.size(50.dp)
//        )
//    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
//        IconButton(
//            onClick = { expanded = !expanded },
//            content = {
                Image(
                    painter = painterResource(id = R.drawable.ic_camcorder),
                    contentDescription = "Icon",
                    modifier = Modifier.size(48.dp)
                )
//            }
//        )

//        if(expanded){
//            IconButton(
//                onClick = { expanded = !expanded },
//                content = {
//                    Image(
//                        painter = painterResource(id = com.example.floatingbubble.R.drawable.ic_close_bubble),
//                        contentDescription = "Icon",
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
//            )
//
//            IconButton(
//                onClick = { expanded = !expanded },
//                content = {
//                    Image(
//                        painter = painterResource(id = com.example.floatingbubble.R.drawable.ic_disclaimer),
//                        contentDescription = "Icon",
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
//            )
//        }
    }
}

@Preview
@Composable
private fun PreviewBubble() {
    BubbleCompose()
}