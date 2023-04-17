package com.example.momentsrecyclerview.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class TweetWithCommentsAndImages(
    @Embedded
    val tweet: LocalTweet,
    @Relation(parentColumn = "id", entityColumn = "tweetId")
    val comments: List<LocalTweetComment>?,
    @Relation(parentColumn = "id", entityColumn = "id")
    val images: List<LocalImage>?
)

@Entity(tableName = "tweet")
data class LocalTweet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val content: String?,
    val senderId: Long
)

@Entity(tableName = "tweet_comment")
data class LocalTweetComment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val content: String,
    val senderId: Long,
    val tweetId: Long
)

@Entity(tableName = "image")
data class LocalImage(
    @PrimaryKey
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    val id: Long
)
