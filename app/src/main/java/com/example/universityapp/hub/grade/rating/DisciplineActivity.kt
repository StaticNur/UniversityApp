package com.example.universityapp.hub.grade.rating

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.databinding.ActivityDisciplineBinding

class DisciplineActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDisciplineBinding
    private lateinit var recyclerView: RecyclerView
    private var disciplineId: Int = 1400122

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisciplineBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerViewRating
        disciplineId = intent.getIntExtra("id",1400122)
        binding.nameDiscipline.text = intent.getStringExtra("title")
        setContentView(binding.root)

        InitView()

    }

    private fun InitView() {
        NetworkRating(binding, this, disciplineId).execute()
    }
}