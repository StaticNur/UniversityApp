package com.example.universityapp.model.dataSchedule

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date


data class TimeTable(
    @JsonProperty("Date")
    val date: Date,
    @JsonProperty("Lessons")
    val lessons: ArrayList<Lesson>
)