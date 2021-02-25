package com.example.newstracker.repository

import com.example.newstracker.room.dao.SavedArticlesDao
import com.example.newstracker.room.entity.SavedArticlesEntity
import javax.inject.Inject

class SavedArticlesRepository @Inject constructor(private val savedArticlesDao: SavedArticlesDao) {

    suspend fun saveArticle(article: SavedArticlesEntity) = savedArticlesDao.saveArticle(article)
    suspend fun checkArticle(article: String) = savedArticlesDao.checkArticle(article)
    suspend fun deleteArticle(article : SavedArticlesEntity) = savedArticlesDao.deleteArticle(article)
    fun getAllSavedArticles() = savedArticlesDao.getAllSavedArticles()
    suspend fun clearSavedArticles() = savedArticlesDao.clearSavedArticles()
}