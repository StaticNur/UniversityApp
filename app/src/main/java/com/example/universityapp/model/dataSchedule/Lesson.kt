package com.example.universityapp.model.dataSchedule

import com.fasterxml.jackson.annotation.JsonProperty


data class Lesson(
    @JsonProperty("Number")
    val number: Int,
    @JsonProperty("SubgroupCount")
    val subgroupCount: Int,
    @JsonProperty("Disciplines")
    val disciplines: ArrayList<Discipline>
)