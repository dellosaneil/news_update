package com.example.newstracker.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newstracker.room.entity.SavedArticlesEntity


@Dao
interface SavedArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article : SavedArticlesEntity)

    @Query("SELECT COUNT(articleTitle) FROM saved_articles_table WHERE articleTitle = :article")
    suspend fun checkArticle(article: String) : Int

    @Delete
    suspend fun deleteArticle(article : SavedArticlesEntity)

    @Query("DELETE FROM saved_articles_table")
    suspend fun clearSavedArticles()

    @Query("SELECT * FROM saved_articles_table WHERE articleTitle LIKE :search OR source LIKE :search ORDER BY time DESC")
    fun searchArticles(search : String) : List<SavedArticlesEntity>?


}