package com.example.newstracker.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.newstracker.Constants
import com.example.newstracker.room.NewsTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, NewsTrackerDatabase::class.java, Constants.DATABASE_NAME).build()


}