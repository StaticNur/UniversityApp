package com.example.universityapp.hub.message

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class ForumMessage(
    @JsonProperty("Id") val id: Int,
    @JsonProperty("User") val user: User,
    @JsonProperty("IsTeacher") val isTeacher: Boolean,
    @JsonProperty("CreateDate") val createDate: Date,
    @JsonProperty("Text") val text: String
)
data class Photo(
    @JsonProperty("UrlSmall") val urlSmall: String?,
    @JsonProperty("UrlMedium") val urlMedium: String?,
    @JsonProperty("UrlSource") val urlSource: String?
)
data class User(
    @JsonProperty("Id") val id: String,
    @JsonProperty("UserName") val userName: String,
    @JsonProperty("FIO") val fIO: String,
    @JsonProperty("Photo") val photo: Photo
)
data class MessageItem(
    val messageDate: Date,
    val messageText: String,
    var isRightMessage: Boolean
)
