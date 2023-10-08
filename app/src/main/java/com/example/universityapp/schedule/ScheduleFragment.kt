package com.example.universityapp.schedule

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R

class ScheduleFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewSchedule)
        return view
        //return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }
    private fun setupRecyclerView() {
        val scheduleList = generateDummyData() // Замените этот вызов на ваш метод получения данных
        scheduleAdapter = ScheduleAdapter(scheduleList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = scheduleAdapter
    }
    private fun generateDummyData(): List<ScheduleItem> {
        // Здесь вы можете создать и вернуть свои данные для расписания
        return listOf(
            ScheduleItem("1", "Математика", "Иванов"),
            ScheduleItem("2", "Физика", "Петров"),
            ScheduleItem("3", "История", "Сидоров"),
            // Добавьте другие элементы по аналогии
        )
    }
}