package com.example.universityapp.messages

import android.os.AsyncTask
import com.example.universityapp.databinding.FragmentMessagesBinding
import com.example.universityapp.model.User
import com.example.universityapp.model.data.Root
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkTask(
    private val button: FragmentMessagesBinding
    //private val email: String,
    //private val password: String
) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String {
        return try {
            val client = OkHttpClient()
            val apiUrl = "https://papi.mrsu.ru/v1/User"
            val email = ""
            val password = ""
            val accessToken = TokenRepository().getAccessToken(email, password)

            val request = Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer $accessToken")
                .build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val objectMapper = ObjectMapper()
                    println("Response root ")
                    val root: Root = objectMapper.readValue(responseBody, Root::class.java)
                    println("Response root $root")
                    val user = User(email, password, accessToken, root)
                    user.toString()
                } else "error"
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun onPostExecute(result: String) {
        println("User response: $result")
    }
}