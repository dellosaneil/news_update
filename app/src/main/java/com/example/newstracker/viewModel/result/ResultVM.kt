package com.example.newstracker.viewModel.result

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstracker.repository.RetrofitRepository
import com.example.newstracker.retrofit.dataclass.NewsResponse
import com.example.newstracker.room.entity.PreferenceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ResultVM(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private val rawArticle: MutableLiveData<Response<NewsResponse>?> = MutableLiveData()

    private lateinit var preferences: PreferenceEntity
    private var finished = MutableLiveData(false)

    fun checkFinished() = finished

    fun placePreferences(pref: PreferenceEntity) {
        Log.i(TAG, "placePreferences: ")
        preferences = pref
    }

    fun getArticles() = rawArticle

    private val TAG = "ResultVM"

    fun retrieveArticles() {
        Log.i(TAG, "retrieveArticles: ")
        viewModelScope.launch(IO) {
            val tempValue =
                retrofitRepository.repositoryArticles(
                    preferences.category,
                    preferences.country,
                    preferences.keyword,
                    preferences.language
                )
            Log.i(
                TAG,
                "\nCategory: ${preferences.category}\nCountry: ${preferences.country}\nKeyword: ${preferences.keyword}\nLanguage: ${preferences.language}"
            )
            withContext(Main) {
                finished.value = true
                rawArticle.value = tempValue
            }
        }
    }

    fun clearAllData(){
        finished.value = false
        rawArticle.value = null
    }
}