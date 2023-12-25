package com.example.universityapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.data.entity.DisciplineXX

class DisciplineAdapter() :
    RecyclerView.Adapter<DisciplineAdapter.ViewHolder>() {
    private var disciplineList: List<DisciplineXX> = ArrayList()
    private lateinit var onItemClick: OnItemClick

    fun setDisciplineList(disciplineList: List<DisciplineXX>){
        this.disciplineList = disciplineList
        notifyDataSetChanged()
    }

    fun setOnClickListener(onItemClick: OnItemClick){
        this.onItemClick = onItemClick
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDiscipline: TextView = itemView.findViewById(R.id.textDiscipline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_discipline, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemName = disciplineList[position]
        holder.textDiscipline.text = itemName.Title

        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(disciplineList[position])
        }
    }

    override fun getItemCount(): Int {
        return disciplineList.size
    }

    interface OnItemClick{
        fun onItemClick(disciplineXX: DisciplineXX)
    }
}