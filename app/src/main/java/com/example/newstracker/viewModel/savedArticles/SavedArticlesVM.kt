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


    private var savedArticles: LiveData<List<SavedArticlesEntity>>? = null

    init {
        viewModelScope.launch(IO) {
            withContext(Main) {
                isFinishedLoading.value = true
            }
        }
    }

    fun isFinished() = isFinishedLoading

    fun restoreDeletedArticle(savedArticle: SavedArticlesEntity) {
        viewModelScope.launch(IO) {
            repository.saveArticle(savedArticle)
        }
    }

    fun searchArticles(search: String) : LiveData<List<SavedArticlesEntity>> {
        val convert = "%$search%"
        return repository.searchArticles(convert)
    }


    fun deleteArticle(savedArticle: SavedArticlesEntity) {
        viewModelScope.launch(IO) { repository.deleteArticle(savedArticle) }
    }
}