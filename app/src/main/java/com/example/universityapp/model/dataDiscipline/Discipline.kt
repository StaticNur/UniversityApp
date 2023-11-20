package com.example.universityapp.model.dataDiscipline

import com.fasterxml.jackson.annotation.JsonProperty

data class Discipline(
    @JsonProperty("Relevance")
    val relevance: Boolean,
    @JsonProperty("IsTeacher")
    val isTeacher: Boolean,
    @JsonProperty("UnreadedCount")
    val unreadedCount: Int,
    @JsonProperty("UnreadedMessageCount")
    val unreadedMessageCount: Int,
    @JsonProperty("Groups")
    val groups: Any?,
    @JsonProperty("DocFiles")
    val docFiles: Any?,
    @JsonProperty("WorkingProgramm")
    val workingProgramm: Any?,
    @JsonProperty("Id")
    val id: Int,
    @JsonProperty("PlanNumber")
    val planNumber: String,
    @JsonProperty("Year")
    val year: String,
    @JsonProperty("Faculty")
    val faculty: String,
    @JsonProperty("EducationForm")
    val educationForm: String,
    @JsonProperty("EducationLevel")
    val educationLevel: String,
    @JsonProperty("Specialty")
    val specialty: String,
    @JsonProperty("SpecialtyCod")
    val specialtyCod: String,
    @JsonProperty("Profile")
    val profile: String,
    @JsonProperty("PeriodString")
    val periodString: String,
    @JsonProperty("PeriodInt")
    val periodInt: Int,
    @JsonProperty("Title")
    val title: String,
    @JsonProperty("Language")
    val language: Any?
)