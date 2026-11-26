package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChatMessageEntity::class,
        LessonProgressEntity::class,
        UserStatsEntity::class,
        DailyActivityEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MiaDatabase : RoomDatabase() {
    abstract fun miaDao(): MiaDao

    companion object {
        @Volatile
        private var INSTANCE: MiaDatabase? = null

        fun getInstance(context: Context): MiaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MiaDatabase::class.java,
                    "mia_talks.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
