package com.example.universityapp.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.adapters.SectionAdapter
import com.example.universityapp.data.entity.Grade
import com.example.universityapp.databinding.ActivityGradeBinding
import com.example.universityapp.mvvm.GradeMVVM
import com.example.universityapp.utils.GradeCustomFactory

class GradeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGradeBinding
    private lateinit var gradeMVVM: GradeMVVM
    private lateinit var sectionAdapter: SectionAdapter
    private lateinit var sPref: SharedPreferences
    private var disciplineId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradeBinding.inflate(layoutInflater)
        disciplineId = intent.getIntExtra("id",1400122)
        sectionAdapter = SectionAdapter()
        binding.nameDiscipline.text = intent.getStringExtra("title")
        setContentView(binding.root)
        sPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        gradeMVVM = ViewModelProvider(this,
            GradeCustomFactory(sPref.getString("saved_token", "").toString())
        )[GradeMVVM::class.java]

        InitView()
    }

    private fun InitView() {
        prepareRecyclerView()
        observeGrade()
        gradeMVVM.getGrade(disciplineId.toString())
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
    private fun observeGrade() {
        gradeMVVM.observeGrade().observe(this, object : Observer<Grade?> {
            override fun onChanged(t: Grade?) {
                if (!t!!.Sections.isEmpty()) {
                    sectionAdapter.setSectionList(t.Sections)
                } else {
                    Toast.makeText(applicationContext, "Не удалось получить оценки", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun prepareRecyclerView() {
        binding.recyclerViewRating.apply {
            adapter = sectionAdapter
            layoutManager = LinearLayoutManager(context) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }

}