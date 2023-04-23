package com.example.momentsrecyclerview.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.TweetComment
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun TweetsItem(
    tweet: Tweet,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) = Row(
    verticalAlignment = Alignment.Top,
    modifier = modifier.padding(
        bottom = dimensionResource(id = R.dimen.tweet_padding_bottom)
    )
) {
    AsyncImage(
        model = tweet.sender.avatarUrl,
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.tweet_padding))
            .fillMaxWidth()
            .weight(1f)
            .aspectRatio(1f / 1f),
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(id = R.string.user_avatar_description),
        placeholder = painterResource(id = R.drawable.loading_img),
        error = painterResource(id = R.drawable.ic_broken_image)
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
        if (tweet.images?.isNotEmpty() == true) {
            GridImages(tweet.images, onImageClick)
        }
        if (tweet.comments?.isNotEmpty() == true) {
            TweetCommentItem(tweet.comments)
        }
    }
}

@Composable
private fun GridImages(
    images: List<ImageUrl>,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val columnCountMap = mapOf(1 to 1, 2 to 2, 4 to 2)
    val columnCount =
        if (columnCountMap.containsKey(images.size)) columnCountMap[images.size]!! else 3
    val rowCount = ((images.size - 1) / 3) + 1
    Column {
        for (i in 0 until rowCount) {
            Row {
                for (j in 0 until columnCount) {
                    val index = i * columnCount + j
                    if (index < images.size) {
                        val url = images[index].url
                        val encodeUrl = URLEncoder.encode(
                            url,
                            StandardCharsets.UTF_8.toString()
                        )
                        AsyncImage(
                            model = url,
                            contentDescription = stringResource(id = R.string.tweet_image_description),
                            modifier = modifier
                                .padding(
                                    end = dimensionResource(id = R.dimen.tweet_padding),
                                    bottom = dimensionResource(id = R.dimen.tweet_padding)
                                )
                                .size(100.dp)
                                .clickable { onImageClick(encodeUrl) },
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.loading_img),
                            error = painterResource(id = R.drawable.ic_broken_image)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TweetCommentItem(
    tweetComments: List<TweetComment>,
    modifier: Modifier = Modifier
) = Column(
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
