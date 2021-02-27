package com.example.newstracker.repository

import com.example.newstracker.room.dao.PreferenceDao
import com.example.newstracker.room.entity.PreferenceEntity
import javax.inject.Inject

class PreferenceRepository @Inject constructor(private val preferenceDao: PreferenceDao) {

    fun searchPreference(search : String) = preferenceDao.searchPreference(search)

    suspend fun addNewPreference(preference: PreferenceEntity) =
        preferenceDao.addPreference(preference)

    suspend fun deletePreference(preference: PreferenceEntity) = preferenceDao.deletePreference(preference)

    suspend fun checkLabel(label: String) = preferenceDao.checkLabel(label)

    suspend fun clearSearchPreferences() = preferenceDao.clearSearchPreferences()

}