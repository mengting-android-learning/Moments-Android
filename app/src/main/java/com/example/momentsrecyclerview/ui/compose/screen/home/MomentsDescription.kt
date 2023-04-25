package com.example.momentsrecyclerview.ui.compose.screen.home

import android.net.Uri
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.ui.MomentsViewModel
import com.example.momentsrecyclerview.ui.STATUS

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
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    var showModelBottomSheet by remember { mutableStateOf(false) }
    LaunchedEffect(showModelBottomSheet) {
        if (showModelBottomSheet) {
            sheetState.show()
        }
        showModelBottomSheet = false
    }
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            // Sheet content
            BottomSheetContent()
        }
    ) {
        // Screen content
        LazyColumn(modifier = modifier.fillMaxHeight()) {
            item {
                UserInfoItem(
                    userInfo,
                    navigateToNewTextTweetScreen,
                    navigateToNewTweetScreen,
                    setLocalImage,
                    persistAccess,
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
private fun BottomSheetContent() {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Space()
            Text(text = "Camera")
            Space()
        }
        Divider(color = Color.Gray, thickness = 1.dp)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Space()
            Text(text = "Choose from Album")
            Space()
        }
        Space(
            modifier = Modifier.background(color = Color.LightGray)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Space()
            Text(text = "Cancel")
            Space()
        }
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
