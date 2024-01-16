package com.example.universityapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class StudentDB(
    val BirthDate: String?,
    val Email: String?,
    val FIO: String?,
    @PrimaryKey
    val Id: String,
    val Photo: String?,
    val StudentCod: String?,
    val TeacherCod: String?,
)