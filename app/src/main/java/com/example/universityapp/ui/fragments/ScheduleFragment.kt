package com.example.universityapp.ui.fragments

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.adapters.ScheduleAdapter
import com.example.universityapp.data.entity.Schedule
import com.example.universityapp.databinding.FragmentScheduleBinding
import com.example.universityapp.mvvm.ScheduleFragMVVM
import com.example.universityapp.utils.ScheduleCustomFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleFragment : Fragment() {
    private lateinit var binding:FragmentScheduleBinding
    private lateinit var myAdapter: ScheduleAdapter
    private lateinit var scheduleMvvm:ScheduleFragMVVM
    private lateinit var sPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = ScheduleAdapter()
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        scheduleMvvm = ViewModelProvider(this,
            ScheduleCustomFactory(sPref.getString("saved_token", "").toString()))[ScheduleFragMVVM::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentScheduleBinding.inflate(inflater,container,false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        scheduleMvvm.getSchedule(currentDate.format(formatter))

        onClickCalender()
        prepareRecyclerView()
        observeSchedule()
        onScheduleClick()
    }

    private fun onClickCalender() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            scheduleMvvm.getSchedule(selectedDate)
            Log.d("CalendarView", "Clicked date: $selectedDate")
        }
    }

    private fun onScheduleClick() {
       /* myAdapter.onItemClicked(object : ScheduleAdapter.OnItemScheduleClicked{
            override fun onClickListener(schedule: Schedule) {
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(CATEGORY_NAME,category.strCategory)
                startActivity(intent)
            }
        })*/
    }

    private fun observeSchedule() {
        scheduleMvvm.observeSchedule().observe(viewLifecycleOwner, object : Observer<Schedule?> {
            override fun onChanged(t: Schedule?) {
                if (!t!![0].TimeTable.Lessons.isEmpty()) {
                    myAdapter.setScheduleList(t[0].TimeTable.Lessons)
                } else {
                    Toast.makeText(requireContext().applicationContext, "No lesson this day", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun prepareRecyclerView() {
        binding.recyclerViewSchedule.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext()) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }
}