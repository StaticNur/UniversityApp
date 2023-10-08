package com.example.universityapp.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R

class ScheduleAdapter(private val scheduleList: List<ScheduleItem>) :
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOrder: TextView = itemView.findViewById(R.id.textOrder)
        val textLesson: TextView = itemView.findViewById(R.id.textLesson)
        val textTeacher: TextView = itemView.findViewById(R.id.textTeacher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = scheduleList[position]
        holder.textOrder.text = item.order
        holder.textLesson.text = item.lesson
        holder.textTeacher.text = item.teacher
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
}