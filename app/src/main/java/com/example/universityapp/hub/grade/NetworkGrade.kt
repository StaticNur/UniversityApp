package com.example.universityapp.hub.grade

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.databinding.ActivityGradeBinding
import com.example.universityapp.model.dataDiscipline.Root
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkGrade(
    private val binding: ActivityGradeBinding,
    private val context: Context,
    private val year: String,
    private val period: Int
) : AsyncTask<Void, Void, String>() {
    private lateinit var sPref: SharedPreferences
    override fun doInBackground(vararg params: Void?): String {
        sPref = context.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        return try {
            val client = OkHttpClient()
            val apiUrl = "https://papi.mrsu.ru/v1/StudentSemester?year=$year&period=$period"//v1/StudentSemester?selector=current"
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
            val root = objectMapper.readValue(result, Root::class.java)
            val disciplineList = root.recordBooks.flatMap { it.disciplines }.map { it.title }
            val disciplineId = root.recordBooks.flatMap { it.disciplines }.map { it.id }
            val gradeAdapter = GradeAdapter(context, disciplineList, disciplineId)
            val recyclerView = binding.recyclerViewGrade
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = gradeAdapter
            if(root.recordBooks.isEmpty()){
                binding.emptyDiscipline.text = "В этот период нет дисциплин"
            }
        }else{
            Toast.makeText(context, "Ошибка на стороне сервера", Toast.LENGTH_SHORT).show()
        }

    }
}