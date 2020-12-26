package com.example.newstracker.viewModel.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstracker.repository.RetrofitRepository
import com.example.newstracker.retrofit.dataclass.NewsResponse
import com.example.newstracker.room.entity.PreferenceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ResultVM(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private val rawArticle: MutableLiveData<Response<NewsResponse>> = MutableLiveData()

    private lateinit var preferences: PreferenceEntity
    private var finished = MutableLiveData(false)

    fun checkFinished() = finished
    fun placePreferences(pref: PreferenceEntity) {
        preferences = pref
    }

    fun getArticles() = rawArticle

    fun retrieveArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            val tempValue =
                retrofitRepository.repositoryArticles(
                    preferences.category,
                    preferences.country,
                    preferences.keyword,
                    preferences.language
                )
            withContext(Main) {
                finished.value = true
                rawArticle.value = tempValue
            }
        }
    }
}