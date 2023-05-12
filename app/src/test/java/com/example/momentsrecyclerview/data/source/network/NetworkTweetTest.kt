package com.example.momentsrecyclerview.data.source.network

import com.squareup.moshi.Moshi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NetworkTweetTest {
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        moshi = Moshi.Builder().build()
    }

    @Test
    fun networkImage_deserialization_verifySuccess() {
        val json = "{ \"url\": \"https://example.com/image.jpg\" }"
        val url = moshi.adapter(NetworkImage::class.java).fromJson(json)
        assertEquals("https://example.com/image.jpg", url?.url)
    }

    @Test
    fun networkSender_deserialization_verifySuccess() {
        val json = """
            {
          "username": "user",
          "nick": "nick",
          "avatar": "http://avater"
        }
        """
        val networkSender = NetworkSender("user", "nick", "http://avater")
        val sender = moshi.adapter(NetworkSender::class.java).fromJson(json)
        assertEquals(networkSender, sender)
    }

    @Test
    fun networkTweetComment_deserialization_verifySuccess() {
        val json = """
                {
                    "content": "Good",
                    "sender": {
                        "username": "user",
                        "nick": "nick",
                        "avatar": "http://avater"
                       }
                }
            """

        val tweetComment = moshi.adapter(NetworkTweetComment::class.java).fromJson(json)
        assertEquals("Good", tweetComment?.content)
        assertEquals("user", tweetComment?.sender?.userName)
        assertEquals("nick", tweetComment?.sender?.nick)
        assertEquals("http://avater", tweetComment?.sender?.avatarUrl)
    }

    @Test
    fun networkTweet_deserialization_verifySuccess() {
        val json = """{
    "content": "content",
    "images": [
      {
        "url": "https://images/1"
      },
      {
        "url": "https://images/2"
      }
    ],
    "sender": {
      "username": "cyao",
      "nick": "Cheng Yao",
      "avatar": "https://tw-mobile-xian.github.io/moments-data/images/user/avatar/001.jpeg"
    },
    "comments": [
      {
        "content": "Good.",
        "sender": {
          "username": "leihuang",
          "nick": "Lei Huang",
          "avatar": "https://tw-mobile-xian.github.io/moments-data/images/user/avatar/002.jpeg"
        }
      }
    ]
  }"""
        val tweet = moshi.adapter(NetworkTweet::class.java).fromJson(json)
        assertEquals("content", tweet?.content)
        assertEquals(2, tweet?.images?.size)
    }
}
