package com.example.momentsrecyclerview.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MomentsDatabaseDao {

    @Insert
    suspend fun insertUser(user: LocalUser)

    @Insert
    suspend fun insertTweet(tweet: LocalTweet)

    @Insert
    suspend fun insertTweetComment(comment: LocalTweetComment)

    @Insert
    suspend fun insertImage(image: LocalImage)

    @Query("SELECT * from tweet")
    suspend fun loadTweets(): List<TweetWithCommentsAndImages>
}
