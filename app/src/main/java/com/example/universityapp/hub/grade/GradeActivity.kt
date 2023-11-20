package com.example.universityapp.hub.grade

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.databinding.ActivityGradeBinding

class GradeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGradeBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradeBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerViewGrade
        setContentView(binding.root)

        InitView()
    }

    private fun InitView() {
        NetworkGrade(binding, this).execute()
        /*val disciplineList  = generateDummyData()
        val gradeAdapter = GradeAdapter(disciplineList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = gradeAdapter*/
    }

    private fun generateDummyData(): List<String> {
        return listOf("Математическая логика и теория алгоритмов",
                "Машинное обучение и большие данные",
                "Методы визуализации данных",
                "Правоведение",
                "Разработка многопоточных приложений",
                "Разработка мобильных приложений",
                "Технологии баз данных",
                "Технологическая (проектно-технологическая) практика",
                "Физика",
                "Функциональный анализ"
        )
    }
}