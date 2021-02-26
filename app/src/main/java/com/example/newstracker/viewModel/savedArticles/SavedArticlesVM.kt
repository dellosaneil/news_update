package com.example.newstracker.viewModel.savedArticles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newstracker.repository.SavedArticlesRepository
import com.example.newstracker.room.entity.SavedArticlesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SavedArticlesVM @Inject constructor(private val repository: SavedArticlesRepository) :
    ViewModel() {

    private var isFinishedLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _articleList = MutableLiveData<List<SavedArticlesEntity>?>()
    fun articleList(): LiveData<List<SavedArticlesEntity>?> = _articleList

    init {
        viewModelScope.launch(IO){
            searchArticles("")
            withContext(Main){
                isFinishedLoading.value = true
            }
        }

    }

    fun isFinished() = isFinishedLoading

    suspend fun restoreDeletedArticle(savedArticle: SavedArticlesEntity) {
        repository.saveArticle(savedArticle)
    }

    fun searchArticles(search: String) {
        val convert = "%$search%"
        viewModelScope.launch(IO) {
            val articles = repository.searchArticles(convert)
            withContext(Main) {
                _articleList.value = articles
            }
        }
    }


    fun deleteArticle(savedArticle: SavedArticlesEntity) {
        viewModelScope.launch(IO) { repository.deleteArticle(savedArticle) }
    }
}