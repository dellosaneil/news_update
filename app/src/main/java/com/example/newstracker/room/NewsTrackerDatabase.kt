package com.example.newstracker.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newstracker.Constants.Companion.DATABASE_NAME
import com.example.newstracker.room.dao.PreferenceDao
import com.example.newstracker.room.entity.PreferenceEntity


@Database(entities = [PreferenceEntity::class], version = 1, exportSchema = false)
abstract class NewsTrackerDatabase : RoomDatabase() {
    abstract fun preferenceDao(): PreferenceDao

    companion object {
        @Volatile
        private var DATABASE_INSTANCE: NewsTrackerDatabase? = null

        fun getDatabase(context: Context): NewsTrackerDatabase {
            val temp = DATABASE_INSTANCE
            if (temp != null) {
                return temp
            }
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NewsTrackerDatabase::class.java,
                DATABASE_NAME
            ).build()
            DATABASE_INSTANCE = instance
            return instance

        }
    }
}