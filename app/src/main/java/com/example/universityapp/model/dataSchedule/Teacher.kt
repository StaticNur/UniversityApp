package com.example.universityapp.model.dataSchedule

import com.fasterxml.jackson.annotation.JsonProperty


data class Teacher(
    @JsonProperty("Id")
    val id: String,
    @JsonProperty("UserName")
    val userName: String,
    @JsonProperty("FIO")
    val fIO: String,
    @JsonProperty("Photo")
    val photo: Photo
)