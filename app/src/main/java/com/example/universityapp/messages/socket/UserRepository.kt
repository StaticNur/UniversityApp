package com.example.universityapp.messages.socket

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class UserRepository {
    fun getUserData(callback: (String) -> Unit) {
        val client = OkHttpClient()
        val apiUrl = "https://papi.mrsu.ru/v1/User"
        val accessToken = "dvYi0AS2LU3hVXILzxAxMkCDZymu3LvU1a1FtoyUMO2-70eAakI-IsUXNNikLwWHz6gQNwLly6jJNEggwWmHFOyLIqm7K5oOk-chN11DvJ8VC3dAVu9IOcDD3fm0ig1CZO2uIEMNgyBSxPPIMOh9vk7heFc-1p2nNImCMadXK5FyGFv2NOfjV5FUd1pY-wOz8to4Sdf0-Rr37hGBfVjCF9VVYVJ930AsVnNACG57KWdNE2m2eeOCxfTapFBSCb2JPlcr2uO1WtQcn-h28h2HOtBXeMVZT7atcVJRKk80HX2rWBO4TXF2GUErqqXDItUvuSjX2im10Bu0bO6T_b4-4xHB7jdMN9k4eHqxgZbHd65xQU5MO59h8iTn-1gdkOcPsf_YaXjVTslpnu8YKAvyfDib9A610eu33rzjraz2tatnGadECicOnMwecRzM3oT3SjFF2_yixS07SIUeusL1kt83wHPOtfGaCQXBFDdpQ4fnnL9FXoszhe7M1a5qDawcvudAR26yjYhfdLpEovDt1PsciAAJ01lBBu7AaHhPjQ1lVFy-6F-Szb6jlBBfwVynsLKwFafbtqJId3bpVVskMqdI9oE"
        val request = Request.Builder()
            .url(apiUrl)
            .header("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: "Empty response body"
                    callback(responseBody)
                } else {
                    callback("Error: ${response.code}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback("Error: ${e.message}")
            }
        })
    }
}