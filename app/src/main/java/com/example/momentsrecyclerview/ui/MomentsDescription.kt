package com.example.momentsrecyclerview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.TweetComment
import com.example.momentsrecyclerview.viewmodels.MomentsViewModel

@Composable
fun MomentsDescription(momentsViewModel: MomentsViewModel) {
    val tweets by momentsViewModel.tweetsList.observeAsState()
    tweets?.let { Moments(tweets = tweets!!) }
//    Moments(getTweets())
}

@Composable
fun TweetCommentItem(modifier: Modifier = Modifier, tweetComments: List<TweetComment>) {
    Column(modifier.background(colorResource(id = R.color.grey))) {
        for (tweetComment in tweetComments) {
            Row(
                modifier = modifier
                    .padding(
                        0.dp,
                        5.dp,
                        10.dp,
                        5.dp
                    )
            ) {
                Text(
                    text = "${tweetComment.sender.nick}:",
                )
                Text(
                    text = tweetComment.content,
                )
            }
        }
    }
}

@Composable
fun TweetsItem(modifier: Modifier = Modifier, tweet: Tweet) =
    Row(verticalAlignment = Alignment.Top) {
        Image(
            modifier = modifier
                .padding(5.dp)
                .size(50.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.user_avatar),
            contentDescription = stringResource(id = R.string.user_avatar_description)
        )
        Column(horizontalAlignment = Alignment.Start) {
            Text(text = tweet.sender.nick)
            tweet.content?.let { Text(text = it, modifier = modifier.padding(bottom = 5.dp)) }
            tweet.images?.let {
                Image(
                    modifier = modifier
                        .padding(bottom = 5.dp)
                        .size(100.dp),
                    painter = painterResource(id = R.drawable.user_profile),
                    contentDescription = stringResource(id = R.string.user_profile_description),
                    contentScale = ContentScale.Crop,
                )
            }
            if (tweet.comments?.isNotEmpty() == true) {
                TweetCommentItem(tweetComments = tweet.comments)
            }
        }
    }

@Composable
fun UserInfoItem(modifier: Modifier = Modifier) =
    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        Column {
            Image(
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp),
                painter = painterResource(id = R.drawable.user_profile),
                contentDescription = stringResource(id = R.string.user_profile_description),
                contentScale = ContentScale.Crop,

            )
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(25.dp)
            )
        }
        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "user name", modifier = modifier.padding(end = 5.dp))
                Image(
                    modifier = modifier
                        .padding(end = 5.dp)
                        .size(75.dp),
                    contentScale = ContentScale.Crop,

                    painter = painterResource(id = R.drawable.user_avatar),
                    contentDescription = stringResource(id = R.string.user_avatar_description)
                )
            }
        }
    }

@Composable
fun Moments(tweets: List<Tweet>) {
    LazyColumn {
        item { UserInfoItem() }
        items(items = tweets) { tweet ->
            TweetsItem(tweet = tweet)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoItemPreview() {
    UserInfoItem()
}

private val sender: Sender = Sender("nick", "nick", "null")
private fun getTweetCommentsList() = listOf(
    TweetComment("comment1", sender),
    TweetComment("comment1", sender)
)

private fun getTweets() = listOf(
    Tweet("content", null, sender, getTweetCommentsList()),
    Tweet("content", listOf(ImageUrl("1")), sender, getTweetCommentsList())
)

@Preview
@Composable
private fun TweetsItemPreview() {
    TweetsItem(tweet = getTweets()[0])
}
