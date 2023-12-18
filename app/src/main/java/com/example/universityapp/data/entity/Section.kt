package com.example.universityapp.data.entity

data class Section(
    val ControlDots: List<ControlDot>,
    val CreateDate: String,
    val CreatorId: String,
    val Description: Any,
    val Id: Int,
    val Order: Int,
    val SectionType: Int,
    val Title: String
)