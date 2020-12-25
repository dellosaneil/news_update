package com.example.newstracker.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "preference_table")
data class PreferenceEntity(
    @PrimaryKey
    val label: String,
    val category : String,
    val country : String,
    val keyword : String,
    val language : String
)