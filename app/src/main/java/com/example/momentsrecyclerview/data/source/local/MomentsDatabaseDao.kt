package com.example.momentsrecyclerview.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MomentsDatabaseDao {

    @Insert
    suspend fun insertUser(user: LocalUser): Long

    @Insert
    suspend fun insertTweet(tweet: LocalTweet): Long

    @Insert
    suspend fun insertTweetComment(comment: LocalTweetComment)

    @Insert
    suspend fun insertImage(images: List<LocalImage>)

    @Query("SELECT * from user WHERE profile_image_url IS NOT NULL")
    suspend fun getUserInfo(): LocalUser

    @Query("SELECT * from tweet")
    @Transaction
    suspend fun loadTweets(): List<LocalEntireTweet>
}
