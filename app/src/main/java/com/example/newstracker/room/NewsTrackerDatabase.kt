package com.example.newstracker.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newstracker.room.dao.PreferenceDao
import com.example.newstracker.room.dao.SavedArticlesDao
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.room.entity.SavedArticlesEntity


@Database(
    entities = [PreferenceEntity::class, SavedArticlesEntity::class],
    version = 6,
    exportSchema = false
)
abstract class NewsTrackerDatabase : RoomDatabase() {
    abstract fun preferenceDao(): PreferenceDao
    abstract fun savedArticlesDao(): SavedArticlesDao
}