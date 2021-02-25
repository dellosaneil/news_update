package com.example.newstracker.viewModel.savedArticles

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstracker.repository.SavedArticlesRepository
import com.example.newstracker.room.entity.SavedArticlesEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedArticlesVM @ViewModelInject constructor(private val repository: SavedArticlesRepository) : ViewModel() {

    private var isFinishedLoading :MutableLiveData<Boolean> = MutableLiveData(false)

    private var savedArticles: LiveData<List<SavedArticlesEntity>>? = null

    init {
        viewModelScope.launch(IO) {
            setSavedArticles()
            withContext(Main){
                isFinishedLoading.value = true
            }
        }
    }

    fun isFinished() = isFinishedLoading

    private fun setSavedArticles() {
        savedArticles = repository.getAllSavedArticles()
    }

    fun getSavedArticles() = savedArticles

    fun deleteArticle(savedArticle : SavedArticlesEntity){
        viewModelScope.launch(IO) { repository.deleteArticle(savedArticle) }
    }
}