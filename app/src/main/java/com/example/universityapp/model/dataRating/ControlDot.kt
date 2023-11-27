package com.example.universityapp.model.dataRating

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class ControlDot(
    @JsonProperty("Mark")
    val mark: Mark?,
    @JsonProperty("Report")
    val report: Any?,
    @JsonProperty("Id")
    val id: Int,
    @JsonProperty("Order")
    val order: Int,
    @JsonProperty("Title")
    val title: String?,
    @JsonProperty("Date")
    val date: Date?,
    @JsonProperty("MaxBall")
    val maxBall: Double,
    @JsonProperty("IsReport")
    val isReport: Boolean,
    @JsonProperty("IsCredit")
    val isCredit: Boolean,
    @JsonProperty("CreatorId")
    val creatorId: String,
    @JsonProperty("CreateDate")
    val createDate: Date?,
    @JsonProperty("TestProfiles")
    val testProfiles: ArrayList<Any?>
)
