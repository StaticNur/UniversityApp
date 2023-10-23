package com.example.universityapp.model.data

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class Root(
    @JsonProperty("Email") val email: String?,
    @JsonProperty("EmailConfirmed") val emailConfirmed: Boolean,
    @JsonProperty("EnglishFIO") val englishFIO: String?,
    @JsonProperty("TeacherCod") val teacherCod: String?,
    @JsonProperty("StudentCod") val studentCod: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("BirthDate")
    val birthDate: Date?,
    @JsonProperty("AcademicDegree") val academicDegree: String?,
    @JsonProperty("AcademicRank") val academicRank: String?,
    @JsonProperty("Roles") val roles: List<Role>?,
    @JsonProperty("Id") val id: String?,
    @JsonProperty("UserName") val userName: String?,
    @JsonProperty("FIO") val fIO: String?,
    @JsonProperty("Photo") val photo: Photo?
)