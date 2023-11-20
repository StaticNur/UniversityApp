package com.example.universityapp.schedule

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.databinding.FragmentScheduleBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var viewModel: ScheduleViewModel
    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewSchedule
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
        // TODO: Use the ViewModel
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            setupRecyclerView(selectedDate)
            Log.d("CalendarView", "Clicked date: $selectedDate")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O) //на более ранних версиях андроид не будет работать
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        setupRecyclerView(currentDate.format(formatter))
    }
    private fun setupRecyclerView(date:String) {
        NetworkSchedule(binding, this.requireContext(), this, date).execute()

        /*val scheduleList = generateDummyData()
        scheduleAdapter = ScheduleAdapter(scheduleList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = scheduleAdapter*/
    }
    private fun generateDummyData(): List<ScheduleItem> {
        return listOf(
            ScheduleItem("1", "Математика", "Иванов"),
            ScheduleItem("2", "Физика", "Петров"),
            ScheduleItem("3", "История", "Сидоров"),
        )
    }
}