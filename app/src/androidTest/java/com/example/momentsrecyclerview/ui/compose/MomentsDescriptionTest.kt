package com.example.momentsrecyclerview.ui.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.ui.compose.screen.home.TweetsItem
import com.example.momentsrecyclerview.ui.compose.screen.home.UserInfoItem
import org.junit.Rule
import org.junit.Test

class MomentsDescriptionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sender = Sender("user", "nick", "https://com.example/avatar")

    @Test
    fun tweetItemTestWithoutImages() {
        val tweet = Tweet("content", null, sender, null)

        composeTestRule.setContent {
            TweetsItem(tweet = tweet, onImageClick = {})
        }

        composeTestRule.onNodeWithText("nick").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("tweet image").assertDoesNotExist()
    }

    @Test
    fun tweetItemTestWithImages() {
        val tweet = Tweet("content", listOf(ImageUrl("https://com.example/image")), sender, null)

        composeTestRule.setContent {
            TweetsItem(tweet = tweet, onImageClick = {})
        }

        composeTestRule.onRoot().printToLog("currentLabelExists")

        composeTestRule.onNodeWithContentDescription("tweet image").assertIsDisplayed()
    }

    @Test
    fun userInfoItemTest() {
        val userInfo = UserInfo(
            "https://example.com/profile.jpg",
            "https://example.com/avatar.jpg",
            "nick",
            "user"
        )

        composeTestRule.setContent {
            UserInfoItem(userInfo, {}, {})
        }

        composeTestRule.onNodeWithText("nick").assertIsDisplayed()
        composeTestRule.onNodeWithText("user").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("this is user profile").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("this is user avatar").assertIsDisplayed()
    }
}