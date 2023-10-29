package com.example.universityapp.model

import com.example.universityapp.model.data.Root

data class User(
    val token: String?,
    val root: Root?
    /*val objectMapper = ObjectMapper()
    val myJsonString = """{"Email": "test@example.com", "EmailConfirmed": true, "EnglishFIO": "John Doe", "StudentCod": "123", "BirthDate": "2023-10-22T00:00:00Z", "Roles": [{"Name": "Admin", "Description": null}], "Id": "1", "UserName": "john_doe", "FIO": "John Doe", "Photo": {"UrlSmall": "small.jpg", "UrlMedium": "medium.jpg", "UrlSource": "source.jpg"}}"""
    // Десериализация JSON в объект
    val root: Root = objectMapper.readValue(myJsonString, Root::class.java)*/
)
