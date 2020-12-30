package com.example.newstracker.viewModel.savedArticles

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newstracker.room.NewsTrackerDatabase
import com.example.newstracker.room.dao.SavedArticlesDao
import com.example.newstracker.room.entity.SavedArticlesEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedArticlesVM(application: Application) : AndroidViewModel(application) {

    private var savedArticlesDao: SavedArticlesDao? =
        NewsTrackerDatabase.getDatabase(application).savedArticlesDao()

    private var isFinishedLoading :MutableLiveData<Boolean> = MutableLiveData(false)

    private val TAG = "SavedArticlesVM"
    private var savedArticles: LiveData<List<SavedArticlesEntity>>? = null

    init {
        Log.i(TAG, "INITIALIZE :  ")
        viewModelScope.launch(IO) {
            setSavedArticles()
            withContext(Main){
                isFinishedLoading.value = true
            }
        }
    }

    fun isFinished() = isFinishedLoading

    private fun setSavedArticles() {
        savedArticles = savedArticlesDao?.getAllSavedArticles()
    }

    fun getSavedArticles() = savedArticles

    fun deleteArticle(title : String){
        viewModelScope.launch(IO) { savedArticlesDao?.deleteArticle(title) }
    }


}