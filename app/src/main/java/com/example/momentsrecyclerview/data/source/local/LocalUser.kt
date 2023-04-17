package com.example.momentsrecyclerview.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class LocalUser(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0L,
    @ColumnInfo(name = "user_name")
    val userName: String,
    val nick: String,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
    @ColumnInfo(name = "profile_image_url")
    val profileImageUrl: String? = null
)
