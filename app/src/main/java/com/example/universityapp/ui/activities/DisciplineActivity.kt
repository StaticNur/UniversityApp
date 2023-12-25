package com.example.universityapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.R
import com.example.universityapp.adapters.DisciplineAdapter
import com.example.universityapp.data.entity.DisciplineX
import com.example.universityapp.data.entity.DisciplineXX
import com.example.universityapp.databinding.ActivityDisciplineBinding
import com.example.universityapp.mvvm.DisciplineMVVM
import com.example.universityapp.utils.DisciplineCustomFactory
import java.time.LocalDate

class DisciplineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisciplineBinding
    private lateinit var disciplineMvvm: DisciplineMVVM
    private lateinit var disciplineAdapter: DisciplineAdapter
    private lateinit var sPref: SharedPreferences
    private lateinit var selectedPeriod: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisciplineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        disciplineAdapter = DisciplineAdapter()
        sPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        disciplineMvvm = ViewModelProvider(this,
            DisciplineCustomFactory(sPref.getString("saved_token", "").toString())
        )[DisciplineMVVM::class.java]
        InitView()
    }

    @RequiresApi(Build.VERSION_CODES.O)//Android 5+
    private fun InitView() {
        prepareRecyclerView()
        observeDiscipline()
        handlerSpinner()
        disciplineAdapter.setOnClickListener(object : DisciplineAdapter.OnItemClick {
            override fun onItemClick(discipline: DisciplineXX) {
                val intentNew:Intent?
                if(intent.getStringExtra("button").equals("message")){
                    intentNew = Intent(this@DisciplineActivity, MessageActivity::class.java)
                }else{
                    intentNew = Intent(this@DisciplineActivity, GradeActivity::class.java)
                    intentNew.putExtra("title", discipline.Title)
                }
                intentNew.putExtra("id", discipline.Id)
                startActivity(intentNew)
            }
        })
        binding.firstSemester.setOnClickListener {
            disciplineMvvm.getDiscipline(selectedPeriod,"1")
        }
        binding.secondSemester.setOnClickListener {
            disciplineMvvm.getDiscipline(selectedPeriod,"2")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlerSpinner() {
        val periodsArray = resources.getStringArray(R.array.periods_array)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, periodsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = binding.kurs
        spinner.adapter = adapter
        val defaultYear = periodsArray.indexOf("${LocalDate.now().year} - ${LocalDate.now().year+1}")
        spinner.setSelection(defaultYear)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                selectedPeriod = periodsArray[position]
                Toast.makeText(this@DisciplineActivity, "Выбран период: $selectedPeriod", Toast.LENGTH_SHORT).show()
                disciplineMvvm.getDiscipline(selectedPeriod,"1")
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    private fun observeDiscipline() {
        disciplineMvvm.observeDiscipline().observe(this, object : Observer<DisciplineX?> {
            override fun onChanged(t: DisciplineX?) {
                if (!t!!.RecordBooks.isEmpty()) {
                    notVisibleEmptyDiscipline()
                    disciplineAdapter.setDisciplineList(t.RecordBooks[0].Disciplines)
                } else {
                    visibleEmptyDiscipline()
                    Toast.makeText(applicationContext, "Не удалось получить дисцеплины", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun visibleEmptyDiscipline() {
        binding.apply {
            disciplines.visibility = View.INVISIBLE
            emptyDiscipline.visibility = View.VISIBLE
        }
    }
    private fun notVisibleEmptyDiscipline() {
        binding.apply {
            disciplines.visibility = View.VISIBLE
            emptyDiscipline.visibility = View.INVISIBLE
        }
    }
    private fun prepareRecyclerView() {
        binding.recyclerViewDiscipline.apply {
            adapter = disciplineAdapter
            layoutManager = LinearLayoutManager(context) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }
}