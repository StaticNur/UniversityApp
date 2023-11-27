package com.example.universityapp.model.dataRating

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class Mark (
    @JsonProperty("Id")
    val id: Any?,
    @JsonProperty("Ball")
    val ball: Double,
    @JsonProperty("CreatorId")
    val creatorId: String,
    @JsonProperty("CreateDate")
    val createDate: Date
)