package com.example.momentsrecyclerview.ui.compose.screen.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.util.MAX_IMAGES_SIZE
import kotlinx.coroutines.launch

@Composable
fun BottomSheetContent(
    persistAccess: (Uri) -> Unit,
    setLocalImage: (List<String>) -> Unit,
    navigateToNewTweetScreen: () -> Unit,
    hideBottomSheet: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        CameraTab()
        Divider(
            color = Color.Gray,
            thickness = dimensionResource(id = R.dimen.divider_weight)
        )
        PhotoPickerTab(
            persistAccess,
            setLocalImage,
            navigateToNewTweetScreen,
            hideBottomSheet
        )
        Space(modifier = Modifier.background(color = Color.LightGray))
        CancelTab(hideBottomSheet)
    }
}

@Composable
private fun CameraTab() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Space()
        Text(text = "Camera")
        Space()
    }
}

@Composable
private fun PhotoPickerTab(
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

@Composable
private fun CancelTab(
    hideBottomSheet: () -> Unit
) {
    val currentHideBottomSheet by rememberUpdatedState(hideBottomSheet)
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            scope.launch {
                currentHideBottomSheet()
            }
        }
    ) {
        Space()
        Text(text = "Cancel")
        Space()
    }
}

@Composable
private fun Space(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.small_margin_end))
    )
}
