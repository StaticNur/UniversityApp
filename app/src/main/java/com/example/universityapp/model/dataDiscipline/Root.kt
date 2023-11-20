package com.example.universityapp.model.dataDiscipline

import com.fasterxml.jackson.annotation.JsonProperty

data class Root(
    @JsonProperty("RecordBooks")
    val recordBooks: List<RecordBook>,
    @JsonProperty("UnreadedDisCount")
    val unreadedDisCount: Int,
    @JsonProperty("UnreadedDisMesCount")
    val unreadedDisMesCount: Int,
    @JsonProperty("Year")
    val year: String,
    @JsonProperty("Period")
    val period: Int
)