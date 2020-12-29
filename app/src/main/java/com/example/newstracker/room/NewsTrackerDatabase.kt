package com.example.newstracker.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newstracker.Constants.Companion.DATABASE_NAME
import com.example.newstracker.room.dao.PreferenceDao
import com.example.newstracker.room.dao.SavedArticlesDao
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.room.entity.SavedArticlesEntity


@Database(
    entities = [PreferenceEntity::class, SavedArticlesEntity::class],
    version = 4,
    exportSchema = false
)
abstract class NewsTrackerDatabase : RoomDatabase() {
    abstract fun preferenceDao(): PreferenceDao
    abstract fun savedArticlesDao(): SavedArticlesDao

    companion object {
        @Volatile
        private var DATABASE_INSTANCE: NewsTrackerDatabase? = null

        fun getDatabase(context: Context): NewsTrackerDatabase {
            val temp = DATABASE_INSTANCE
            if (temp != null) {
                Log.i("NewsTrackerDatabase", "getDatabase: Retrieving")
                return temp
            }
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NewsTrackerDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
            Log.i("NewsTrackerDatabase", "getDatabase: Creating")
            DATABASE_INSTANCE = instance
            return instance
        }
    }
}