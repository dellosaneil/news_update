package com.example.newstracker.dependencyInjection

import com.example.newstracker.room.NewsTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {


    @Provides
    @ActivityScoped
    fun provideSavedArticlesDao(database : NewsTrackerDatabase) = database.savedArticlesDao()

    @Provides
    @ActivityScoped
    fun provideSavedPreferenceDao(database: NewsTrackerDatabase) = database.preferenceDao()
}