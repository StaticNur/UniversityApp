package com.example.universityapp.ui.activities

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.adapters.SecurityAdapter
import com.example.universityapp.data.entity.Security
import com.example.universityapp.databinding.ActivitySecurityBinding
import com.example.universityapp.mvvm.SecurityMVVM
import com.example.universityapp.utils.SecurityCustomFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SecurityActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySecurityBinding
    private lateinit var securityMVVM: SecurityMVVM
    private lateinit var securityAdapter: SecurityAdapter
    private lateinit var sPref: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        securityAdapter = SecurityAdapter()
        //binding.nameDiscipline.text = intent.getStringExtra("title")
        setContentView(binding.root)
        sPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        securityMVVM = ViewModelProvider(this,
            SecurityCustomFactory(sPref.getString("saved_token", "").toString())
        )[SecurityMVVM::class.java]

        InitView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun InitView() {
        prepareRecyclerView()
        observeGrade()
        onClickCalender()
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        securityMVVM.getSecurity(currentDate.format(formatter))
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
    private fun onClickCalender() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            securityMVVM.getSecurity(selectedDate)
            Log.d("CalendarView", "Clicked date: $selectedDate")
        }
    }
    private fun observeGrade() {
        securityMVVM.observeSecurity().observe(this, object : Observer<Security?> {
            override fun onChanged(t: Security?) {
                if (!t!!.isEmpty()) {
                    securityAdapter.setSecurityList(t)
                    notVisibleEmptyDiscipline()
                } else {
                    visibleEmptyDiscipline()
                    Toast.makeText(applicationContext, "Не удалось получить события турикетов", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun visibleEmptyDiscipline() {
        binding.apply {
            touriquetEvents.visibility = View.INVISIBLE
            emptyEvent.visibility = View.VISIBLE
        }
    }
    private fun notVisibleEmptyDiscipline() {
        binding.apply {
            touriquetEvents.visibility = View.VISIBLE
            emptyEvent.visibility = View.INVISIBLE
        }
    }
    private fun prepareRecyclerView() {
        binding.recyclerViewSecurity.apply {
            adapter = securityAdapter
            layoutManager = LinearLayoutManager(context) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }

}