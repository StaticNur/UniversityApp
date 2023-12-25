package com.example.universityapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDB(
    @PrimaryKey
    val Id: String,
    val FIO: String,
    val Photo: String,
    val UserName: String
)