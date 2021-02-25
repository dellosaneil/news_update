package com.example.newstracker.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_articles_table")
data class SavedArticlesEntity(
    @PrimaryKey
    val articleTitle : String,
    val articleDescription : String? = null,
    val articleLink : String? = null,
    val source : String? = null,
    val urlImage : String? = null,
    val time : Long = System.currentTimeMillis()
)
