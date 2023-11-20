package com.example.universityapp.model.dataDiscipline

import com.fasterxml.jackson.annotation.JsonProperty

data class RecordBook(
    @JsonProperty("Cod")
    val cod: String,
    @JsonProperty("Number")
    val number: String,
    @JsonProperty("Faculty")
    val faculty: String,
    @JsonProperty("Disciplines")
    val disciplines: List<Discipline>
)