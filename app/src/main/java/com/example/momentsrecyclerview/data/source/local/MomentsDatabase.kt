package com.example.momentsrecyclerview.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LocalUser::class, LocalTweet::class, LocalTweetComment::class, LocalImage::class],
    version = 1,
    exportSchema = false
)
abstract class MomentsDatabase : RoomDatabase() {
    abstract val momentsDatabaseDao: MomentsDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: MomentsDatabase? = null
        fun getInstance(context: Context): MomentsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MomentsDatabase::class.java,
                        "moment_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
