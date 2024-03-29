package com.example.momentsrecyclerview.ui.compose.screen.add

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.util.CANCEL_TEXT
import com.example.momentsrecyclerview.util.MAX_IMAGES_SIZE
import com.example.momentsrecyclerview.util.POST_TEXT
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NewTweet(
    images: List<String>,
    setLocalImages: (List<String>) -> Unit,
    content: String,
    setLocalContent: (String) -> Unit,
    saveNewTweet: () -> Unit,
    navigateBack: () -> Unit,
    navigateToSingleImage: (String) -> Unit
) {
    val context = LocalContext.current
    if (images.isNotEmpty()) {
        var openPhotoPicker: Boolean by remember { mutableStateOf(false) }
        val launcher = if (MAX_IMAGES_SIZE - images.size > 1) {
            rememberLauncherForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia(MAX_IMAGES_SIZE - images.size)
            ) { uris ->
                uris.forEach {
                    context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                setLocalImages(images + uris.map { it.toString() })
            }
        } else {
            rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) {
                it?.let {
                    context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    setLocalImages(images + it.toString())
                }
            }
        }
        LaunchedEffect(openPhotoPicker) {
            if (openPhotoPicker) {
                launcher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
            openPhotoPicker = false
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.user_avatar_padding),
                        end = dimensionResource(id = R.dimen.user_avatar_padding)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = CANCEL_TEXT,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            setLocalContent("")
                            setLocalImages(emptyList())
                            navigateBack()
                        }
                )
                Button(
                    onClick = {
                        saveNewTweet()
                        navigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Green,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    Text(text = POST_TEXT)
                }
            }
            TextField(
                value = content,
                onValueChange = {
                    setLocalContent(it)
                },
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(id = R.dimen.new_tweet_padding),
                        end = dimensionResource(id = R.dimen.new_tweet_padding)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Column(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.new_tweet_padding),
                    end = dimensionResource(id = R.dimen.new_tweet_padding)
                )
            ) {
                for (i in 0 until 3) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (j in 0 until 3) {
                            val index = i * 3 + j
                            Box(
                                modifier = Modifier
                                    .weight(1f / 3f)
                                    .aspectRatio(1f)
                            ) {
                                if (index < images.size) {
                                    AsyncImage(
                                        model = images[index],
                                        contentDescription = stringResource(id = R.string.tweet_image_description),
                                        modifier = Modifier
                                            .clickable {
                                                val encodeUrl = URLEncoder.encode(
                                                    images[index],
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                navigateToSingleImage(encodeUrl)
                                            }
                                            .padding(
                                                end = dimensionResource(id = R.dimen.tweet_padding),
                                                bottom = dimensionResource(id = R.dimen.tweet_padding)
                                            )
                                            .fillMaxSize(),
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                                if (index == images.size) {
                                    Image(
                                        painter = painterResource(id = R.drawable.plus),
                                        contentDescription = "tap to add image",
                                        modifier = Modifier
                                            .padding(
                                                end = dimensionResource(id = R.dimen.tweet_padding),
                                                bottom = dimensionResource(id = R.dimen.tweet_padding)
                                            )
                                            .fillMaxSize()
                                            .clickable { openPhotoPicker = true }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
