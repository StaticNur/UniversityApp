package com.example.universityapp.model.news

import com.fasterxml.jackson.annotation.JsonProperty

data class News(
    @JsonProperty("Id") val id: Int?,
    @JsonProperty("Date")val date: String?,
    @JsonProperty("Text") val text: String?,
    @JsonProperty("Header") val header: String?,
    @JsonProperty("Viewed") val viewed: Boolean?
)