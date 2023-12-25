package com.example.universityapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.data.entity.SecurityItem
import java.text.SimpleDateFormat
import java.util.Locale

class SecurityAdapter() : RecyclerView.Adapter<SecurityAdapter.ViewHolder>() {

    private var turnstileList: List<SecurityItem> = ArrayList()

    fun setSecurityList(turnstileList: List<SecurityItem>){
        this.turnstileList = turnstileList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val turnstileItem = turnstileList[position]
        val dateFormat = SimpleDateFormat("HH:mm:ss.SSSSSSS", Locale.getDefault())
        val date = dateFormat.parse(turnstileItem.Time)
        val formattedDate = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
        holder.dateEvent.text = formattedDate
        holder.nameBuilding.text = turnstileItem.Build
        holder.stateEvent.text = if(turnstileItem.Status.equals("Вход")){
            "Выход"
        }else "Вход"
    }

    override fun getItemCount(): Int {
        return turnstileList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateEvent: TextView = itemView.findViewById(R.id.textOrder)
        var nameBuilding: TextView = itemView.findViewById(R.id.textLesson)
        var stateEvent: TextView = itemView.findViewById(R.id.textTeacher)
    }
}