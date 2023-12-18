package com.example.universityapp.data.retrofit

import com.example.universityapp.data.entity.DisciplineX
import com.example.universityapp.data.entity.Grade
import com.example.universityapp.data.entity.News
import com.example.universityapp.data.entity.Schedule
import com.example.universityapp.data.entity.Student
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MrsuApi {
    @FormUrlEncoded
    @POST("OAuth/Token")
    fun getNewToken(
        @Header("Authorization") authorization: String?,
        @Field("grant_type") grantType: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Call<ResponseBody>

    @GET("v1/User")
    fun getStudent(
        @Header("Authorization") token: String?
    ): Call<Student>

    @GET("v1/StudentTimeTable")
    fun getSchedule(
        @Header("Authorization") token: String?,
        @Query("date") date:String?
    ): Call<Schedule>

    @GET("v1/News")
    fun getNews(
        @Header("Authorization") token: String?
    ): Call<News>

    @GET("v1/StudentSemester")
    fun getDiscipline(
        @Header("Authorization") token: String?,
        @Query("year") year:String?,
        @Query("period") period:String?
    ): Call<DisciplineX>

    @GET("v1/StudentRatingPlan/{studentId}")
    fun getGrade(
        @Header("Authorization") token: String?,
        @Path("studentId") studentId: String?,
    ): Call<Grade>
















    @GET("v1/ForumMessage")
    fun getForumMessages(
        @Header("Authorization") token: String?,
        @Query("disciplineId") disciplineId: Int,
        @Query("count") count: Int,
        @Query("startMessageId") startMessageId: Int,
    ): Call<ResponseBody>
}
