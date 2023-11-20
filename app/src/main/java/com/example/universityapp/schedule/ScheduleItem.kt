package com.example.universityapp.schedule

data class ScheduleItem(
    val order: String, //номер урока
    val lesson: String, // называние урока
    val teacher: String // фио преподователя
)