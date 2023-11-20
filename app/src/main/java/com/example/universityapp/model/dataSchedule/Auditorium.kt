package com.example.universityapp.model.dataSchedule

import com.fasterxml.jackson.annotation.JsonProperty


data class Auditorium(
    @JsonProperty("Id")
    val id: Int,
    @JsonProperty("Number")
    val number: String,
    @JsonProperty("Title")
    val title: String,
    @JsonProperty("CampusId")
    val campusId: Int,
    @JsonProperty("CampusTitle")
    val campusTitle: String
)