package com.example.universityapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.data.entity.ControlDot

class ControlPointAdapter() : RecyclerView.Adapter<ControlPointAdapter.ViewHolder>() {

    private var controlPoints: List<ControlDot> = ArrayList()

    fun setControlPointList(controlPoints: List<ControlDot>) {
        this.controlPoints = controlPoints
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_rating_control_point, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val controlPoint = controlPoints[position]
        holder.assignmentNameTextView.text = controlPoint.Title
        holder.haveBall.text = if (controlPoint.Mark != null) {
            controlPoint.Mark.Ball.toString()
        } else "0.0"
        holder.maxBall.text = " / ${controlPoint.MaxBall}"
    }

    override fun getItemCount(): Int {
        return controlPoints.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var assignmentNameTextView: TextView = itemView.findViewById(R.id.assignmentNameTextView)
        var haveBall: TextView = itemView.findViewById(R.id.haveBall)
        var maxBall: TextView = itemView.findViewById(R.id.maxBall)
    }
}