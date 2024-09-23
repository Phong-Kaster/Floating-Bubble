package com.example.floatingbubble.ui.fragment.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpack.core.CoreFragment
import com.example.jetpack.core.CoreLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : CoreFragment() {

    @Composable
    override fun ComposeView() {
        super.ComposeView()
        HomeLayout()
    }
}

@Composable
fun HomeLayout() {
    CoreLayout(
        content = {}
    )
}

@Preview
@Composable
private fun PreviewHomeLayout() {
    HomeLayout()
}