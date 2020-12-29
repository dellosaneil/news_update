package com.example.newstracker.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_articles_table")
data class SavedArticlesEntity(
    @PrimaryKey
    val articleTitle : String,
    val articleDescription : String,
    val articleLink : String,
    val source : String
)
