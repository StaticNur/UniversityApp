package com.example.universityapp.data.entity

import androidx.room.Entity

@Entity(tableName = "schedule", primaryKeys = ["Date", "LessonNumber"])
data class ScheduleItemDB (
    val FacultyName: String?,
    val Group: String?,
    val PlanNumber: String?,
    val Date: String = "date", // Часть первичного ключа
    val LessonNumber: Int = 0, // Часть первичного ключа
    val SubgroupCount: Int?,
    val CampusId: Int?,
    val CampusTitle: String?,
    val AuditoriumId: Int?,
    val AuditoriumNumber: String?,
    val Title: String?,
    val DisciplineGroup: String?,
    val DisciplineId: Int?,
    val Language: String?,
    val LessonType: Int?,
    val Remote: Int?,
    val SubgroupNumber: Int?,
    val FIO: String?,
    val TeacherId: String?,
    val Photo: String?,
    val UserName: String?,
    val TimeTableBlockd: Int?
)