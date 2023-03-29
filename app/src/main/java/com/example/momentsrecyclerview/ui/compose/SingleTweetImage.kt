package com.example.momentsrecyclerview.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SingleTweetImage(url: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        url?.let {
            GlideImage(
                model = url,
                contentDescription = "singleImage",
                modifier = Modifier.fillMaxWidth().align(Alignment.Center)
            )
        }
    }
}
