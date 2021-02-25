package com.example.newstracker.viewModel.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstracker.repository.RetrofitRepository
import com.example.newstracker.retrofit.dataclass.Article
import com.example.newstracker.retrofit.dataclass.NewsResponse
import com.example.newstracker.room.entity.PreferenceEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class ResultVM(private val retrofitRepository: RetrofitRepository) : ViewModel() {

    private var rawArticle: MutableLiveData<Response<NewsResponse>?>? = MutableLiveData()
    private val _articleList = MutableLiveData<List<Article>>()

    fun articleList(): LiveData<List<Article>> = _articleList
    fun saveArticleList(list: List<Article>) {
        _articleList.value = list
    }

    private var preferences: PreferenceEntity? = null
    private var finished: MutableLiveData<Boolean>? = null


    fun checkFinished(): LiveData<Boolean>? {
        if (finished == null) {
            finished = MutableLiveData()
        }
        return finished
    }

    fun placePreferences(pref: PreferenceEntity) {
        preferences = pref
    }

    fun getArticles(): LiveData<Response<NewsResponse>?>? {
        if (rawArticle == null) {
            rawArticle = MutableLiveData()
        }
        return rawArticle
    }

    suspend fun retrieveArticles(pageSize: Int = 100) {
        val tempValue =
            retrofitRepository.repositoryArticles(
                preferences!!.category,
                preferences!!.country,
                preferences!!.keyword,
                preferences!!.language,
                pageSize
            )
        withContext(Main) {
            finished?.value = true
            rawArticle?.value = tempValue
        }
    }

    fun clearAllData() {
        finished?.value = null
        rawArticle = null
        preferences = null
        onCleared()
    }
}