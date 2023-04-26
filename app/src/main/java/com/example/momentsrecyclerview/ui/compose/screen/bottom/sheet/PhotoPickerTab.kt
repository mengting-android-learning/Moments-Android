package com.example.momentsrecyclerview.ui.compose.screen.bottom.sheet

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.momentsrecyclerview.util.MAX_IMAGES_SIZE
import kotlinx.coroutines.launch

@Composable
fun PhotoPickerTab(
    persistAccess: (Uri) -> Unit,
    setLocalImage: (List<String>) -> Unit,
    navigateToNewTweetScreen: () -> Unit,
    hideBottomSheet: () -> Unit
) {
    val currentHideBottomSheet by rememberUpdatedState(hideBottomSheet)
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                MAX_IMAGES_SIZE
            )
        ) { uris ->
            if (uris.isNotEmpty()) {
                navigateToNewTweetScreen()
                uris.forEach { persistAccess(it) }
                setLocalImage(uris.map { uri -> uri.toString() })
            }
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            scope.launch {
                currentHideBottomSheet()
                launcher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
    ) {
        Space()
        Text(text = "Choose from Album")
        Space()
    }
}