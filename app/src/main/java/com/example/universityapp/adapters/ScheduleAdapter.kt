package com.example.universityapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.data.entity.Lesson
import com.example.universityapp.data.entity.Schedule

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    private var scheduleList: List<Lesson> = ArrayList()
    private lateinit var onItemClick: OnItemScheduleClicked
    private lateinit var onLongCategoryClick: OnLongCategoryClick

    fun setScheduleList(scheduleList: List<Lesson>){
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }

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
        holder.textOrder.text = item.Number.toString()
        holder.textLesson.text = item.Disciplines[0].Title
        holder.textTeacher.text = item.Disciplines[0].Teacher.FIO
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    interface OnItemScheduleClicked{
        fun onClickListener(item: List<Lesson>)
    }

    fun onItemClicked(onItemClick: OnItemScheduleClicked){
        this.onItemClick = onItemClick
    }

    interface OnLongCategoryClick{
        fun onCategoryLongCLick(schedule:Schedule)
    }
}