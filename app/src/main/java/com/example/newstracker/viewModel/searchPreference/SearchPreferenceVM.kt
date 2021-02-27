package com.example.newstracker.viewModel.searchPreference

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstracker.repository.PreferenceRepository
import com.example.newstracker.room.entity.PreferenceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchPreferenceVM @Inject constructor(private val repository: PreferenceRepository) : ViewModel() {

    private val _preferences = MutableLiveData<List<PreferenceEntity>>()

    val preferences : LiveData<List<PreferenceEntity>> = _preferences

    init{
        searchPreference()
    }

    fun searchPreference(search : String = ""){
        val convert = "%$search%"
        viewModelScope.launch(IO){
            val tempPreferences = repository.searchPreference(convert)
            withContext(Main){
                _preferences.value = tempPreferences
            }
        }
    }

    suspend fun restorePreference(preference: PreferenceEntity) = repository.addNewPreference(preference)

    suspend fun deletePreference(preference : PreferenceEntity) = repository.deletePreference(preference)

}