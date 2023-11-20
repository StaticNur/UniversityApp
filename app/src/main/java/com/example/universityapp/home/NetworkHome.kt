package com.example.universityapp.home

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.universityapp.databinding.FragmentHomeBinding
import com.example.universityapp.model.User
import com.example.universityapp.model.dataUser.Root
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkHome(
    private val binding: FragmentHomeBinding,
    private val context: Context,
    private val homeFragment: HomeFragment) : AsyncTask<Void, Void, String>() {
    private lateinit var sPref: SharedPreferences
    override fun doInBackground(vararg params: Void?): String {
        sPref = context.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        return try {
            val client = OkHttpClient()
            val apiUrl = "https://papi.mrsu.ru/v1/User"
            val accessToken = sPref.getString("saved_token", "")
            println(accessToken)
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
                    val user = User(accessToken, root)
                    responseBody.toString()
                } else "error"
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun onPostExecute(result: String) {
        if(!result.equals("error")){
            val objectMapper = ObjectMapper()
            val root: Root = objectMapper.readValue(result, Root::class.java)
            binding.tvNameStudent.text = root.fIO
            val ivAvatar: ImageView = binding.ivAvatar
            val imageUrl: String = root.photo!!.urlSmall.toString()
            Glide.with(homeFragment)
                .load(imageUrl)
                .circleCrop()
                .into(ivAvatar)
        }else{
            Toast.makeText(context, "Не актуальный токен или проблема с сервером", Toast.LENGTH_SHORT).show()
        }
    }
}