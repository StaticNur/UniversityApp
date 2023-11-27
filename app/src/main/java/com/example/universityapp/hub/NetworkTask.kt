package com.example.universityapp.hub

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.databinding.FragmentHubBinding
import com.example.universityapp.model.news.News
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkTask(
    private val binding: FragmentHubBinding,
    private val context: Context) : AsyncTask<Void, Void, List<News>?>() {
    private lateinit var sPref: SharedPreferences
    override fun doInBackground(vararg params: Void?): List<News>? {
        sPref = context.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        return try {
            val client = OkHttpClient()
            val apiUrl = "https://papi.mrsu.ru/v1/News"
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
                    println("Response root " + responseBody)
                    val newsList: List<Map<String, Any>> = objectMapper.readValue(responseBody, object : TypeReference<List<Map<String, Any>>>() {})
                    var result: MutableList<News> = ArrayList()
                    for (newsMap in newsList) {
                        val id: Int = newsMap["Id"] as Int
                        val date: String = newsMap["Date"] as String
                        val text: String = newsMap["Text"] as String
                        val header: String = newsMap["Header"] as String
                        val viewed: Boolean = newsMap["Viewed"] as Boolean
                        var news: News = News(id, date, text, header, viewed)
                        result.add(news)
                    }
                    if (result.isNotEmpty()) {
                        val firstTenNews: List<News> = result.take(10)
                        firstTenNews
                    } else {
                        println("Ошибка десериализации")
                        null
                    }
                } else null
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun onPostExecute(result: List<News>?) {
        if(result != null){
            val newsAdapter = AnswerAdapter(result)
            val recyclerView = binding.recyclerViewNews
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = newsAdapter
        }else{
            Toast.makeText(context, "Не удалось получить новости", Toast.LENGTH_SHORT).show()
        }
    }
}