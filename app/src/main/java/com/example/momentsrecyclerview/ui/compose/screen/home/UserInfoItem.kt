package com.example.momentsrecyclerview.ui.compose.screen.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.util.MAX_IMAGES_SIZE

@Composable
fun UserInfoItem(
    userInfo: UserInfo,
    navigateToNewTextTweetScreen: () -> Unit,
    navigateToNewTweetScreen: () -> Unit,
    setLocalImage: (List<String>) -> Unit,
    persistAccess: (Uri) -> Unit,
    showBottomSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentShowBottomSheet by rememberUpdatedState(showBottomSheet)
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                MAX_IMAGES_SIZE
            )
        ) { uris ->
            run {
                uris.forEach { persistAccess(it) }
                setLocalImage(uris.map { uri -> uri.toString() })
                if (uris.isNotEmpty()) navigateToNewTweetScreen()
            }
        }
    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        Column {
            Box {
                AsyncImage(
                    model = userInfo.profileImageUrl,
                    modifier = modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentDescription = stringResource(id = R.string.user_profile_description),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            end = dimensionResource(id = R.dimen.icon_end_padding),
                            top = dimensionResource(id = R.dimen.tweet_avatar_size)
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = stringResource(id = R.string.camera_icon_description),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(dimensionResource(id = R.dimen.icon_size))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { navigateToNewTextTweetScreen() },
                                    onTap = {
                                        currentShowBottomSheet()
//                                        scope.launch {
//                                            launcher.launch(
//                                                PickVisualMediaRequest(
//                                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
//                                                )
//                                            )
//                                        }
                                    }
                                )
                            }
                    )
                }
            }
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.icon_size))
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = userInfo.nick,
                modifier = modifier.padding(dimensionResource(id = R.dimen.user_avatar_padding)),
                color = Color.White
            )
            AsyncImage(
                model = userInfo.avatarUrl,
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.user_avatar_padding))
                    .size(dimensionResource(id = R.dimen.user_avatar_size)),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(id = R.string.user_avatar_description),
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.ic_broken_image)
            )
        }
    }
}
