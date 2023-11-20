package com.example.universityapp.hub.grade.rating

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.databinding.ActivityDisciplineBinding
import com.example.universityapp.model.dataRating.ControlPoint
import com.example.universityapp.model.dataRating.SectionItem

class DisciplineActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDisciplineBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisciplineBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerViewRating
        setContentView(binding.root)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val disciplineItems: List<SectionItem> = createSampleData() // Создайте свои данные
        val disciplineAdapter = SectionAdapter(disciplineItems)
        recyclerView.adapter = disciplineAdapter
    }

    private fun createSampleData(): List<SectionItem> {
        val disciplineItems = ArrayList<SectionItem>()
        val controlPoints1 = listOf(
            ControlPoint("Задание 1", 5),
            ControlPoint("Задание 2", 4)
        )
        val disciplineItem1 = SectionItem("Раздел 1", controlPoints1)
        disciplineItems.add(disciplineItem1)
        val controlPoints2 = listOf(
            ControlPoint("Задание 1", 3),
            ControlPoint("Задание 2", 5)
        )
        val disciplineItem2 = SectionItem("Раздел 2", controlPoints2)
        disciplineItems.add(disciplineItem2)
        return disciplineItems
    }
}