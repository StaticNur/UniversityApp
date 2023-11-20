package com.example.universityapp.model.dataSchedule

import com.fasterxml.jackson.annotation.JsonProperty


data class Discipline(
    @JsonProperty("Id")
    val id: Int,
    @JsonProperty("Title")
    val title: String,
    @JsonProperty("Language")
    val language: String?,
    @JsonProperty("LessonType")
    val lessonType: Int,
    @JsonProperty("Remote")
    val remote: Boolean,
    @JsonProperty("Group")
    val group: String,
    @JsonProperty("SubgroupNumber")
    val subgroupNumber: Int,
    @JsonProperty("Teacher")
    val teacher: Teacher,
    @JsonProperty("Auditorium")
    val auditorium: Auditorium
)
