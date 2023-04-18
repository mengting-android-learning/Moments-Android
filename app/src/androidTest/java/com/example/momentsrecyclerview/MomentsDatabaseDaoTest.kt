package com.example.momentsrecyclerview

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.momentsrecyclerview.data.source.local.LocalImage
import com.example.momentsrecyclerview.data.source.local.LocalTweet
import com.example.momentsrecyclerview.data.source.local.LocalTweetComment
import com.example.momentsrecyclerview.data.source.local.LocalUser
import com.example.momentsrecyclerview.data.source.local.MomentsDatabase
import com.example.momentsrecyclerview.data.source.local.MomentsDatabaseDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MomentsDatabaseDaoTest {
    private lateinit var momentsDao: MomentsDatabaseDao
    private lateinit var db: MomentsDatabase
    private val localUser =
        LocalUser(
            userName = "userName",
            nick = "nick",
            avatarUrl = "avatarUrl",
            profileImageUrl = null
        )
    private val user = localUser.copy(profileImageUrl = "profile_url")
    private val tweet = LocalTweet(content = "content", senderId = 1)
    private val comment = LocalTweetComment(content = "content", senderId = 1, tweetId = 1)
    private val image = LocalImage(imageUrl = "imageUrl", tweetId = 1)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MomentsDatabase::class.java
        ).build()
        momentsDao = db.momentsDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertUser() {
        runTest {
            val userId = momentsDao.insertUser(localUser)
            assertEquals(userId, 1)
            val users = momentsDao.getUsers()
            assertEquals(users[0], localUser.copy(userId = 1))
            assertEquals(momentsDao.getUserInfo(), null)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertUserWithProfile() {
        runTest {
            val userId = momentsDao.insertUser(user)
            assertEquals(userId, 1)
            val userInfo = momentsDao.getUserInfo()
            assertEquals(userInfo, user.copy(userId = 1))
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertTweet() {
        runTest {
            val tweetId = momentsDao.insertTweet(tweet)
            val tweets = momentsDao.getTweets()
            assertEquals(tweetId, 1)
            assertEquals(tweets[0], tweet.copy(id = 1))
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertComment() {
        runTest {
            momentsDao.insertTweetComment(comment)
            val comments = momentsDao.getComments()
            assertEquals(comments[0], comment.copy(id = 1))
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertImage() {
        runTest {
            momentsDao.insertImage(listOf(image))
            val images = momentsDao.getImages()
            assertEquals(images[0], image.copy(imageId = 1))
        }
    }

    @Test
    @Throws(Exception::class)
    fun queryEntireTweets() {
        runTest {
            momentsDao.insertUser(localUser)
            momentsDao.insertTweet(tweet)
            momentsDao.insertTweetComment(comment)
            momentsDao.insertImage(listOf(image))
            val tweets = momentsDao.loadTweets()
            assertEquals(tweets[0].tweet.content, tweet.content)
            assertEquals(tweets[0].sender, localUser.copy(userId = 1))
            assertEquals(tweets[0].comments?.size, 1)
            assertEquals(tweets[0].comments?.get(0)?.comment, comment.copy(id = 1))
            assertEquals(tweets[0].comments?.get(0)?.sender, localUser.copy(userId = 1))
            assertEquals(tweets[0].images?.size, 1)
            assertEquals(tweets[0].images?.get(0), image.copy(imageId = 1))
        }
    }
}