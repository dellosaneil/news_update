package com.example.newstracker.viewModel

import androidx.lifecycle.LiveData
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

class MainVM(private val repository: Repository) : ViewModel() {
    private val rawArticle: MutableLiveData<Response<NewsResponse>> = MutableLiveData()

    fun getArticles() = rawArticle

    fun retrieveArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            val tempValue = repository.retrieveArticles()
            withContext(Main){
                rawArticle.value = tempValue
            }
        }
    }
}