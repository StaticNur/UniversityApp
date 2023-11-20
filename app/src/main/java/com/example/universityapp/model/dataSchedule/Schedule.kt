package com.example.universityapp.model.dataSchedule

import com.fasterxml.jackson.annotation.JsonProperty



data class Schedule(
    @JsonProperty("Group")
    val group: String,
    @JsonProperty("PlanNumber")
    val planNumber: String,
    @JsonProperty("FacultyName")
    val facultyName: String,
    @JsonProperty("TimeTableBlockd")
    val timeTableBlockd: Int,
    @JsonProperty("TimeTable")
    val timeTable: TimeTable
)
