package com.example.universityapp.hub.message

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ForumApi {
    @GET("v1/ForumMessage")
    fun getForumMessages(
        @Header("Authorization") token: String?,
        @Query("disciplineId") disciplineId: Int,
        @Query("count") count: Int,
        @Query("startMessageId") startMessageId: Int,
    ): Call<ResponseBody>

    @POST("v1/ForumMessage")
    fun postForumMessage(
        @Query("disciplineId") disciplineId: Int,
    ): Call<ForumMessage>
}
/*
object ForumApiClient {
    val forumApi: ForumApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://papi.mrsu.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ForumApi::class.java)
    }
}*/
