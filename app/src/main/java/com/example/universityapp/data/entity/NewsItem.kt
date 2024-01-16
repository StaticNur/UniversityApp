package com.example.universityapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsItem(
    @PrimaryKey
    val Id: Int,
    val Date: String,
    val Text: String,
    val Header: String,
    val Viewed: Boolean
)