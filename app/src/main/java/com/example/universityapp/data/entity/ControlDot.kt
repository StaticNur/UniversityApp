package com.example.universityapp.data.entity

data class ControlDot(
    val CreateDate: String,
    val CreatorId: String,
    val Date: String,
    val Id: Int,
    val IsCredit: Boolean,
    val IsReport: Boolean,
    val Mark: Mark,
    val MaxBall: Double,
    val Order: Int,
    val Report: Report,
    val TestProfiles: List<Any>,
    val Title: String
)