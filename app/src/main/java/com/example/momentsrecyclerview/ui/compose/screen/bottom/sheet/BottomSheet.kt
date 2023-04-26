package com.example.momentsrecyclerview.ui.compose.screen.bottom.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.example.momentsrecyclerview.R

@Composable
fun BottomSheetContent(
    setLocalImage: (List<String>) -> Unit,
    navigateToNewTweetScreen: () -> Unit,
    hideBottomSheet: () -> Unit,
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        CameraTab(
            hideBottomSheet,
            setLocalImage,
            navigateToNewTweetScreen,
        )
        Divider(
            color = Color.Gray,
            thickness = dimensionResource(id = R.dimen.divider_weight)
        )
        PhotoPickerTab(
            setLocalImage,
            navigateToNewTweetScreen,
            hideBottomSheet
        )
        Space(modifier = Modifier.background(color = Color.LightGray))
        CancelTab(hideBottomSheet)
    }
}

@Composable
fun Space(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.small_margin_end))
    )
}
