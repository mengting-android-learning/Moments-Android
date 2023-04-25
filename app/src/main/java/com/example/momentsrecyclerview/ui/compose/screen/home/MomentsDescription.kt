package com.example.momentsrecyclerview.ui.compose.screen.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.ui.MomentsViewModel
import com.example.momentsrecyclerview.ui.STATUS
import com.example.momentsrecyclerview.util.MAX_IMAGES_SIZE
import kotlinx.coroutines.launch

@Composable
fun MomentsDescription(
    momentsViewModel: MomentsViewModel,
    navigateToSingleTweetImage: (String) -> Unit,
    navigateToNewTextTweetScreen: () -> Unit,
    navigateToNewTweetScreen: () -> Unit,
) {
    var currentStatus by remember { mutableStateOf(STATUS.LOADING) }
    when (currentStatus) {
        STATUS.DONE -> {
            val userInfo by momentsViewModel.userInfo.observeAsState()
            val tweets by momentsViewModel.tweetsList.observeAsState()
            if (userInfo != null && tweets != null) {
                Moments(
                    userInfo!!,
                    tweets!!,
                    navigateToSingleTweetImage,
                    navigateToNewTextTweetScreen,
                    navigateToNewTweetScreen,
                    momentsViewModel::setLocalImages,
                    momentsViewModel::persistAccess
                )
            }
        }
        else -> LoadingOrErrorScreen(
            { status -> currentStatus = status },
            momentsViewModel
        )
    }
}

@Composable
fun LoadingOrErrorScreen(
    onStatusChange: (status: STATUS) -> Unit,
    momentsViewModel: MomentsViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        val currentOnStatusChange by rememberUpdatedState(onStatusChange)
        val status by momentsViewModel.status.observeAsState()
        LaunchedEffect(status) {
            when (status) {
                STATUS.LOADING -> currentOnStatusChange(STATUS.LOADING)
                STATUS.DONE -> currentOnStatusChange(STATUS.DONE)
                STATUS.ERROR -> currentOnStatusChange(STATUS.ERROR)
                else -> {}
            }
        }
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        if (status == STATUS.LOADING) {
            Image(
                painterResource(id = R.drawable.loading_img),
                contentDescription = "loading",
                modifier = modifier
                    .fillMaxSize()
                    .rotate(rotation)
            )
        }
        if (status == STATUS.ERROR) {
            Image(
                painterResource(id = R.drawable.ic_connection_error),
                contentDescription = "loading",
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Moments(
    userInfo: UserInfo,
    tweets: List<Tweet>,
    navigateToSingleTweetImage: (String) -> Unit,
    navigateToNewTextTweetScreen: () -> Unit,
    navigateToNewTweetScreen: () -> Unit,
    setLocalImage: (List<String>) -> Unit,
    persistAccess: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    var showModelBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {
            it != ModalBottomSheetValue.HalfExpanded
        }
    )
    LaunchedEffect(showModelBottomSheet) {
        if (showModelBottomSheet) {
            sheetState.show()
        } else
            sheetState.hide()
    }
    LaunchedEffect(sheetState.isVisible) {
        showModelBottomSheet = sheetState.isVisible
    }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            BottomSheetContent(
                persistAccess,
                setLocalImage,
                navigateToNewTweetScreen
            ) { showModelBottomSheet = false }
        }
    ) {
        LazyColumn(modifier = modifier.fillMaxHeight()) {
            item {
                UserInfoItem(
                    userInfo,
                    navigateToNewTextTweetScreen,
                    { showModelBottomSheet = true }
                )
            }
            items(items = tweets) { tweet ->
                TweetsItem(tweet = tweet, onImageClick = navigateToSingleTweetImage)
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
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
