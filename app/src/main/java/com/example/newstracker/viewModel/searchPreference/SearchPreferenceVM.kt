package com.example.newstracker.viewModel.searchPreference

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.newstracker.repository.DatabaseRepository
import com.example.newstracker.room.NewsTrackerDatabase
import com.example.newstracker.room.entity.PreferenceEntity


class SearchPreferenceVM(application: Application) : AndroidViewModel(application) {

    private val allPreferences : LiveData<List<PreferenceEntity>>
    private val repository : DatabaseRepository

    init{
        val dao = NewsTrackerDatabase.getDatabase(application).preferenceDao()
        repository = DatabaseRepository(dao)
        allPreferences = repository.allData
    }

    fun retrieveAllPreference() = allPreferences

    suspend fun deletePreference(label : String) = repository.deletePreference(label)

}