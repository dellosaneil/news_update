package com.example.newstracker.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstracker.repository.Repository
import com.example.newstracker.retrofit.dataclass.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ResultVM(private val repository: Repository) : ViewModel() {
    private val rawArticle: MutableLiveData<Response<NewsResponse>> = MutableLiveData()

    fun getArticles() = rawArticle

    fun retrieveArticles(
        category: String = "",
        country: String = "ph",
        keyword: String = "",
        language: String = "en"
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val tempValue =
                repository.repositoryArticles(category, country, keyword, language)
            withContext(Main) {
                rawArticle.value = tempValue
            }
        }
    }
}