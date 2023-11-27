package com.example.universityapp.hub.grade

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.databinding.ActivityGradeBinding
import java.time.LocalDate

class GradeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGradeBinding
    private lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradeBinding.inflate(layoutInflater)
        recyclerView = binding.recyclerViewGrade
        setContentView(binding.root)

        InitView()
    }

    @RequiresApi(Build.VERSION_CODES.O)//Android 5+
    private fun InitView() {
        // Получите массив периодов из ресурсов
        val periodsArray = resources.getStringArray(R.array.periods_array)
        // Создайте адаптер для Spinner
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, periodsArray)
        // Установите стиль выпадающего списка
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Найдите Spinner по его идентификатору
        val spinner = binding.kurs
        // Установите адаптер для Spinner
        spinner.adapter = adapter

        var defaultYear = periodsArray.indexOf("${LocalDate.now().year} - ${LocalDate.now().year+1}")
        spinner.setSelection(defaultYear)
        // Добавьте слушателя для обработки выбора элемента
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedPeriod = periodsArray[position]
                Toast.makeText(this@GradeActivity, "Выбран период: $selectedPeriod", Toast.LENGTH_SHORT).show()
                getSemesterDiscipline(selectedPeriod)
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                //getSemesterDiscipline(periodsArray[0])
            }
        }
    }

    private fun getSemesterDiscipline(year:String){
        println(year)
        NetworkGrade(binding, this, year, 1).execute()
        binding.firstSemester.setOnClickListener {
            NetworkGrade(binding, this, year, 1).execute()
        }
        binding.secondSemester.setOnClickListener {
            NetworkGrade(binding, this, year, 2).execute()
        }
    }

}