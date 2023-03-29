package com.example.momentsrecyclerview.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.TweetComment
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.ui.MomentsViewModel
import com.example.momentsrecyclerview.ui.STATUS

@Composable
fun MomentsDescription(momentsViewModel: MomentsViewModel) {
    var showLandingScreen by remember { mutableStateOf(true) }
    if (showLandingScreen) {
        LandingScreen(
            onTimeout = { showLandingScreen = false },
            momentsViewModel = momentsViewModel
        )
    } else {
        val userInfo by momentsViewModel.userInfo.observeAsState()
        val tweets by momentsViewModel.tweetsList.observeAsState()
        if (userInfo != null && tweets != null) {
            Moments(userInfo = userInfo!!, tweets = tweets!!)
        }
    }
}

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    onTimeout: () -> Unit,
    momentsViewModel: MomentsViewModel
) {
    Box(modifier = modifier.fillMaxSize()) {
        val currentOnTimeout by rememberUpdatedState(onTimeout)
        val status by momentsViewModel.status.observeAsState()
        LaunchedEffect(status) {
            if (status == STATUS.DONE) {
                currentOnTimeout()
            }
        }
        Image(
            painterResource(id = R.drawable.loading_img),
            contentDescription = "loading",
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun TweetCommentItem(modifier: Modifier = Modifier, tweetComments: List<TweetComment>) =
    Column(
        modifier
            .fillMaxWidth()
            .padding(
                end = dimensionResource(id = R.dimen.small_margin_end),
                top = dimensionResource(id = R.dimen.tweet_padding)
            )
            .background(colorResource(id = R.color.grey))
    ) {
        for (tweetComment in tweetComments) {
            Row {
                Text(
                    text = "${tweetComment.sender.nick}: ",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = tweetComment.content,
                    fontSize = 16.sp
                )
            }
        }
    }

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TweetsItem(modifier: Modifier = Modifier, tweet: Tweet) =
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.padding(
            bottom = dimensionResource(id = R.dimen.tweet_padding_bottom)
        )
    ) {
        GlideImage(
            model = tweet.sender.avatarUrl,
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.tweet_padding))
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(1f / 1f),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.user_avatar_description)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxWidth()
                .weight(9f)
                .padding(
                    start = dimensionResource(id = R.dimen.tweet_padding),
                    end = dimensionResource(id = R.dimen.small_margin_end)
                )
        ) {
            Text(
                text = tweet.sender.nick,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            tweet.content?.let { Text(text = it, modifier = modifier.padding(bottom = 5.dp)) }
            tweet.images?.let {
                GridImages(tweet.images, modifier)
            }
            if (tweet.comments?.isNotEmpty() == true) {
                TweetCommentItem(tweetComments = tweet.comments)
            }
        }
    }

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun GridImages(
    images: List<ImageUrl>,
    modifier: Modifier
) {
    val columnCountMap = mapOf(1 to 1, 2 to 2, 4 to 2)
    val columnCount =
        if (columnCountMap.containsKey(images.size)) columnCountMap[images.size]!! else 3
    val rowCount = ((images.size - 1) / 3) + 1
    Column {
        for (i in 0 until rowCount) {
            Row {
                for (j in 0 until columnCount) {
                    GlideImage(
                        model = images[i * columnCount + j].url,
                        contentDescription = stringResource(id = R.string.tweet_image_description),
                        modifier = modifier
                            .padding(
                                end = dimensionResource(id = R.dimen.tweet_padding),
                                bottom = dimensionResource(id = R.dimen.tweet_padding)
                            )
                            .size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserInfoItem(modifier: Modifier = Modifier, userInfo: UserInfo) =
    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        Column {
            GlideImage(
                model = userInfo.profileImageUrl,
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentDescription = stringResource(id = R.string.user_profile_description),
                contentScale = ContentScale.Crop,

            )
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
            GlideImage(
                model = userInfo.avatarUrl,
                modifier = modifier
                    .padding(end = 10.dp)
                    .size(75.dp),
                contentScale = ContentScale.Crop,
                contentDescription = stringResource(id = R.string.user_avatar_description)
            )
        }
    }

@Composable
fun Moments(modifier: Modifier = Modifier, userInfo: UserInfo, tweets: List<Tweet>) =
    LazyColumn(modifier = modifier.fillMaxHeight()) {
        item { UserInfoItem(userInfo = userInfo) }
        items(items = tweets) { tweet ->
            TweetsItem(tweet = tweet)
        }
    }
