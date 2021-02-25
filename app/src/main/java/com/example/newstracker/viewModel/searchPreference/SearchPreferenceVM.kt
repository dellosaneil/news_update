package com.example.newstracker.viewModel.searchPreference

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.newstracker.repository.PreferenceRepository
import com.example.newstracker.room.entity.PreferenceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchPreferenceVM @Inject constructor(private val repository: PreferenceRepository) : ViewModel() {

    private val allPreferences : LiveData<List<PreferenceEntity>> = repository.getAllPreferences()

    fun retrieveAllPreference() = allPreferences

    suspend fun deletePreference(preference : PreferenceEntity) = repository.deletePreference(preference)

}