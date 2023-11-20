package com.example.universityapp.hub.grade.rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.model.dataRating.ControlPoint

class ControlPointAdapter(private val controlPoints: List<ControlPoint>) :
    RecyclerView.Adapter<ControlPointAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_rating_control_point, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val controlPoint = controlPoints[position]
        holder.assignmentNameTextView.text = controlPoint.assignmentName
        holder.scoreTextView.text = controlPoint.score.toString()
    }

    override fun getItemCount(): Int {
        return controlPoints.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var assignmentNameTextView: TextView = itemView.findViewById(R.id.assignmentNameTextView)
        var scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
    }
}