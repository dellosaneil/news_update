package com.example.newstracker.viewModel.addSearchPreference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstracker.repository.PreferenceRepository
import com.example.newstracker.room.entity.PreferenceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class AddSearchPreferenceVM @Inject constructor(private val searchPreferences: PreferenceRepository) :
    ViewModel() {


    private var canSave = false

    private fun indexNumber(key: String, array: Array<String>) = array.indexOf(key)

    fun checkPreferenceLabel(preferenceLabel: String): Boolean {
        return preferenceLabel.isNotBlank()
    }

    fun processPreferenceValues(
        rawPreferenceStrings: Array<String>,
        preferenceKeys: Array<Array<String>>,
        preferenceValues: Array<Array<String>>
    ): Int {
        val countryIndex = indexNumber(rawPreferenceStrings[3], preferenceKeys[0])
        val languageIndex = indexNumber(rawPreferenceStrings[4], preferenceKeys[1])
        var countryCode =
            if (countryIndex == -1) preferenceValues[0][0] else preferenceValues[0][countryIndex]
        var languageCode =
            if (languageIndex == -1) preferenceValues[1][0] else preferenceValues[1][languageIndex]
        countryCode = convertAll("All", countryCode)
        val categoryCode = convertAll("Any", rawPreferenceStrings[2])
        languageCode = convertAll("Any", languageCode)
        if (rawPreferenceStrings[1] != "") {
            canSave = true
        }
        return if (canSave) {
            saveToRoomDatabase(
                rawPreferenceStrings[0],
                rawPreferenceStrings[1],
                categoryCode,
                countryCode,
                languageCode
            )
        }else{
            2
        }
    }

    private fun saveToRoomDatabase(
        preferenceLabel: String,
        keyword: String,
        categoryCode: String,
        countryCode: String,
        languageCode: String
    ): Int {
        val state = viewModelScope.async(Dispatchers.IO) {
            val newPreference = PreferenceEntity(
                preferenceLabel,
                categoryCode,
                countryCode,
                keyword,
                languageCode
            )
            val canUpdate = searchPreferences.checkLabel(preferenceLabel)
            if (canUpdate == 0) {
                searchPreferences.addNewPreference(newPreference)
                return@async 0
            } else {
                canSave = false
                return@async 1
            }
        }
        var code : Int
        runBlocking {
            code = state.await()
        }
        return code
    }

    private fun convertAll(defaultText: String, inputValue: String): String {
        val query: String
        if (defaultText != inputValue && inputValue != "") {
            query = inputValue
            canSave = true
        } else {
            query = ""
        }
        return query
    }


}




















