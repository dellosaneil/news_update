package com.example.newstracker.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "preference_table")
data class PreferenceEntity(
    @PrimaryKey
    val label: String,
    val category : String,
    val country : String,
    val keyword : String,
    val language : String
): Parcelable