package com.example.momentsrecyclerview.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class LocalEntireTweet(
    @Embedded
    val tweet: LocalTweet,
    @Relation(parentColumn = "senderId", entityColumn = "userId")
    val sender: LocalUser,
    @Relation(parentColumn = "id", entityColumn = "tweetId", entity = LocalTweetComment::class)
    val comments: List<CommentWithSender>?,
    @Relation(parentColumn = "id", entityColumn = "tweetId")
    val images: List<LocalImage>?
)

data class CommentWithSender(
    @Embedded
    val comment: LocalTweetComment,
    @Relation(parentColumn = "senderId", entityColumn = "userId")
    val sender: LocalUser
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
    val tweetId: Long,
)

@Entity(tableName = "image")
data class LocalImage(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0L,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    val tweetId: Long
)
