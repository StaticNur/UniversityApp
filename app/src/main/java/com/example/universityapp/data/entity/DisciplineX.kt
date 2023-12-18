package com.example.universityapp.data.entity

data class DisciplineX(
    val Period: Int,
    val RecordBooks: List<RecordBook>,
    val UnreadedDisCount: Int,
    val UnreadedDisMesCount: Int,
    val Year: String
)