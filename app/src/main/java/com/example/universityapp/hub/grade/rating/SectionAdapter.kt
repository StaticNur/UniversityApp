package com.example.universityapp.hub.grade.rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.model.dataRating.SectionItem

class SectionAdapter(private val disciplineItems: List<SectionItem>) :
    RecyclerView.Adapter<SectionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_rating_section, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disciplineItem = disciplineItems[position]
        holder.sectionNameTextView.text = disciplineItem.sectionName

        val controlPointAdapter = ControlPointAdapter(disciplineItem.controlPoints)
        holder.controlPointRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.controlPointRecyclerView.adapter = controlPointAdapter
    }

    override fun getItemCount(): Int {
        return disciplineItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sectionNameTextView: TextView = itemView.findViewById(R.id.sectionNameTextView)
        var controlPointRecyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewСontrolPoint)
    }
}