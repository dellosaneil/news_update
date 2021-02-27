package com.example.newstracker.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newstracker.room.entity.PreferenceEntity

@Dao
interface PreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPreference(preference : PreferenceEntity)

    @Query("SELECT COUNT(label) FROM preference_table WHERE label = :newLabel")
    suspend fun checkLabel(newLabel : String) : Int

    @Query("SELECT * FROM preference_table WHERE label LIKE :search OR country LIKE :search OR keyword LIKE :search OR language LIKE :search ORDER BY time")
    fun searchPreference(search : String) : List<PreferenceEntity>

    @Delete
    suspend fun deletePreference(preference: PreferenceEntity)

    @Query("DELETE FROM preference_table")
    suspend fun clearSearchPreferences()

}