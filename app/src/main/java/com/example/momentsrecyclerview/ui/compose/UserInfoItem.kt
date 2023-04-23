package com.example.momentsrecyclerview.ui.compose

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.UserInfo
import kotlinx.coroutines.launch

@Composable
fun UserInfoItem(
    userInfo: UserInfo,
    navigateToNewTextTweetScreen: () -> Unit,
    navigateToNewTweetScreen: () -> Unit,
    setLocalImage: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                9
            )
        ) { uris ->
            run {
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
                        .padding(end = 30.dp, top = 50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = stringResource(id = R.string.camera_icon_description),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(25.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { navigateToNewTextTweetScreen() },
                                    onTap = {
                                        scope.launch {
                                            launcher.launch(
                                                PickVisualMediaRequest(
                                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                                )
                                            )
                                        }
                                    }
                                )
                            }
                    )
                }
            }
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(25.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = userInfo.nick,
                modifier = modifier.padding(end = 10.dp),
                color = Color.White
            )
            AsyncImage(
                model = userInfo.avatarUrl,
                modifier = modifier
                    .padding(end = 10.dp)
                    .size(75.dp),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(id = R.string.user_avatar_description),
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.ic_broken_image)
            )
        }
    }
}
