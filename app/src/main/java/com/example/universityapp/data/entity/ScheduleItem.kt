package com.example.universityapp.data.entity

data class ScheduleItem(
    val FacultyName: String,
    val Group: String,
    val PlanNumber: String,
    val TimeTable: TimeTable,
    val TimeTableBlockd: Int
)