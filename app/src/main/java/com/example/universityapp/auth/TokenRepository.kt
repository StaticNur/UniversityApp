package com.example.universityapp.auth

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class TokenRepository {
    fun getAccessToken(username: String, password: String): String {
        val clientId = "8"
        val clientSecret = "qweasd"
        val tokenUrl = "https://p.mrsu.ru/OAuth/Token"
        val credentials = "$clientId:$clientSecret"
        val base64Credentials = android.util.Base64.encodeToString(
            credentials.toByteArray(),
            android.util.Base64.NO_WRAP
        )

        val requestBody = FormBody.Builder()
            .add("grant_type", "password")
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url(tokenUrl)
            .header("Authorization", "Basic $base64Credentials")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        println(response)
        return if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: "Empty response body"
            println("Access Token Response: $responseBody")
            val jsonObject = JSONObject(responseBody)
            val accessToken = jsonObject.getString("access_token")
            println("Access token: $accessToken")
            accessToken
        } else {
            "500"
        }
    }
}