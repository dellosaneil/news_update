package com.example.newstracker.viewModel.searchPreference

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.newstracker.repository.PreferenceRepository
import com.example.newstracker.room.entity.PreferenceEntity


class SearchPreferenceVM @ViewModelInject constructor(private val repository: PreferenceRepository) : ViewModel() {

    private val allPreferences : LiveData<List<PreferenceEntity>> = repository.getAllPreferences()

    fun retrieveAllPreference() = allPreferences

    suspend fun deletePreference(preference : PreferenceEntity) = repository.deletePreference(preference)

}