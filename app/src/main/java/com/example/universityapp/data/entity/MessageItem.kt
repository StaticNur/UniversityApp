package com.example.universityapp.data.entity

data class MessageItem(
    var CreateDate: String?,
    val Id: Int?,
    val IsTeacher: Boolean?,
    val Text: String,
    val User: User?
)