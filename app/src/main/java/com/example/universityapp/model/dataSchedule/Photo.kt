package com.example.universityapp.model.dataSchedule

import com.fasterxml.jackson.annotation.JsonProperty


data class Photo(
    @JsonProperty("UrlSmall")
    val urlSmall: String,
    @JsonProperty("UrlMedium")
    val urlMedium: String,
    @JsonProperty("UrlSource")
    val urlSource: String
)