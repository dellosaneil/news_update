package com.example.newstracker.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newstracker.room.entity.SavedArticlesEntity


@Dao
interface SavedArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article : SavedArticlesEntity)

    @Query("SELECT COUNT(articleTitle) FROM saved_articles_table WHERE articleTitle = :article")
    suspend fun checkArticle(article: String) : Int

    @Query("DELETE FROM saved_articles_table WHERE articleTitle = :article")
    suspend fun deleteArticle(article : String)

    @Query("SELECT * FROM saved_articles_table")
    fun getAllSavedArticles() : LiveData<List<SavedArticlesEntity>>


}