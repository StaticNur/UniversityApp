package com.example.universityapp.model.dataRating

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class Section(
    @JsonProperty("ControlDots")
    val controlDots: ArrayList<ControlDot>,
    @JsonProperty("SectionType")
    val sectionType: Int,
    @JsonProperty("Id")
    val id: Int,
    @JsonProperty("Order")
    val order: Int,
    @JsonProperty("Title")
    val title: String,
    @JsonProperty("Description")
    val description: String?,
    @JsonProperty("CreatorId")
    val creatorId: String,
    @JsonProperty("CreateDate")
    val createDate: Date
)