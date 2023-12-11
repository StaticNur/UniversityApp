package com.example.universityapp.auth

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class RefreshAccessToken {
    fun refreshAccessToken(refreshToken: String): String {
        val clientId = "8"
        val clientSecret = "qweasd"
        val tokenUrl = "https://p.mrsu.ru/OAuth/Token"
        val credentials = "$clientId:$clientSecret"
        val base64Credentials = android.util.Base64.encodeToString(
            credentials.toByteArray(),
            android.util.Base64.NO_WRAP
        )

        val requestBody = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("refresh_token", refreshToken)
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
            println("Refreshed Token Response: $responseBody")
            val jsonObject = JSONObject(responseBody)
            val newAccessToken = jsonObject.getString("access_token")
            val newRefreshToken = jsonObject.getString("refresh_token")
            println("New Access token: $newAccessToken")
            println("New Refresh token: $newRefreshToken")

            //newAccessToken
            responseBody
        } else {
            "500"
        }
    }
}