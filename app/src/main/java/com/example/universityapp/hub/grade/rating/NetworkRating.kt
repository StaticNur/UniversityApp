package com.example.universityapp.hub.grade.rating

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.databinding.ActivityDisciplineBinding
import com.example.universityapp.model.dataRating.ControlPoint
import com.example.universityapp.model.dataRating.Root
import com.example.universityapp.model.dataRating.SectionItem
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkRating(
    private val binding: ActivityDisciplineBinding,
    private val context: Context,
    private val disciplineId: Int
) : AsyncTask<Void, Void, String>() {
    private lateinit var sPref: SharedPreferences
    override fun doInBackground(vararg params: Void?): String {
        sPref = context.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        return try {
            val client = OkHttpClient()
            val apiUrl = "https://papi.mrsu.ru/v1/StudentRatingPlan/$disciplineId"
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
        val objectMapper = ObjectMapper()
        val root = objectMapper.readValue(result, Root::class.java)
        val disciplineSectionList = root.sections

        val disciplineItems = ArrayList<SectionItem>()
        var sumBall:Double = 0.0
        var sumMaxBall:Double = 0.0
        for (section in disciplineSectionList){
            val controlPoints = ArrayList<ControlPoint>()
            for (task in section.controlDots){
                val title: String? = task.title
                val haveBall: Double? = task.mark?.ball
                val maxBall: Double = task.maxBall
                controlPoints.add(ControlPoint(title ?: "-", haveBall ?: 0.0, maxBall))
                sumBall += (haveBall ?: 0.0)
                sumMaxBall += maxBall
            }
            disciplineItems.add(SectionItem(section.title, controlPoints))
        }
        val originalDouble = sumBall
        val formattedString = String.format("%.2f", originalDouble)
        val resultDouble = formattedString.toDouble()
        disciplineItems.add(SectionItem("Итого: ", listOf(ControlPoint("итого:",resultDouble,sumMaxBall))))
        if(root.markZeroSession != null){
            disciplineItems.add(SectionItem("Оценка за нулевую сессию : ", listOf(ControlPoint("оценка:",root.markZeroSession.ball,5.0))))
        }
        var recyclerView = binding.recyclerViewRating
        recyclerView.layoutManager = LinearLayoutManager(context)
        val disciplineAdapter = SectionAdapter(disciplineItems)
        recyclerView.adapter = disciplineAdapter
    }
}