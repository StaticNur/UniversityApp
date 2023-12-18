package com.example.universityapp.data.entity

data class Student(
    val AcademicDegree: String?,
    val AcademicRank: String?,
    val BirthDate: String?,
    val Email: String?,
    val EmailConfirmed: Boolean,
    val EnglishFIO: String?,
    val FIO: String?,
    val Id: String?,
    val Photo: Photo?,
    val Roles: List<Role>?,
    val StudentCod: String?,
    val TeacherCod: String?,
    val UserName: String?
)