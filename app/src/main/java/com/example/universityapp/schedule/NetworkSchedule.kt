package com.example.universityapp.schedule

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.databinding.FragmentScheduleBinding
import com.example.universityapp.model.dataSchedule.Schedule
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkSchedule(
    private val binding: FragmentScheduleBinding,
    private val context: Context,
    private val homeFragment: ScheduleFragment,
    private val date: String) : AsyncTask<Void, Void, String>() {
    private lateinit var sPref: SharedPreferences
    override fun doInBackground(vararg params: Void?): String {
        sPref = context.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        return try {
            val client = OkHttpClient()
            val apiUrl = "https://papi.mrsu.ru/v1/StudentTimeTable?date=$date"
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
                    println("Response body $responseBody")
                    println("Response root ")

                    val scheduleListType = objectMapper.typeFactory.constructCollectionType(List::class.java, Schedule::class.java)
                    val root: List<Schedule> = objectMapper.readValue(responseBody, scheduleListType)

                    println("Response root $root")
                    responseBody.toString()
                } else "error"
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun onPostExecute(result: String) {
        val objectMapper = ObjectMapper()
        val scheduleListType = objectMapper.typeFactory.constructCollectionType(List::class.java, Schedule::class.java)
        val scheduleList: List<Schedule> = objectMapper.readValue(result, scheduleListType)
        val lessonList = parseScheduleList(scheduleList)
        println("После парсинга $lessonList")

        /*val scheduleList = listOf(
            ScheduleItem("1", "Математика", "Иванов"),
            ScheduleItem("2", "Физика", "Петров"),
            ScheduleItem("3", "История", "Сидоров"),
        )*/

        val scheduleAdapter = ScheduleAdapter(lessonList)
        val recyclerView = binding.recyclerViewSchedule
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = scheduleAdapter
    }

    private fun parseScheduleList(scheduleList: List<Schedule>): List<ScheduleItem> {
        val resultList = mutableListOf<ScheduleItem>()
        for (schedule in scheduleList) {
            val lessons = schedule.timeTable.lessons
            for (lesson in lessons) {
                for (discipline in lesson.disciplines) {
                    val scheduleItem = ScheduleItem(
                        order = lesson.number.toString(),
                        lesson = discipline.title,
                        teacher = discipline.teacher.fIO
                    )
                    resultList.add(scheduleItem)
                }
            }
        }
        return resultList
    }
}