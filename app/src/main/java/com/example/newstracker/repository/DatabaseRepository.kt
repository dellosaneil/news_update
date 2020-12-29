package com.example.newstracker.repository

import androidx.lifecycle.LiveData
import com.example.newstracker.room.dao.PreferenceDao
import com.example.newstracker.room.entity.PreferenceEntity

class DatabaseRepository(private val preferenceDao: PreferenceDao) {

    val allData: LiveData<List<PreferenceEntity>> = preferenceDao.getAllSavedPreference()

    suspend fun addNewPreference(preference: PreferenceEntity) =
        preferenceDao.addPreference(preference)

    suspend fun deletePreference(label: String) = preferenceDao.deletePreference(label)

    suspend fun checkLabel(label: String) = preferenceDao.checkLabel(label)
    
}