package com.example.universityapp.hub.grade

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.hub.grade.rating.DisciplineActivity

class GradeAdapter(private val context: Context,
                   private val disciplineList: List<String>,
                   private val disciplineId: List<Int>) :
    RecyclerView.Adapter<GradeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDiscipline: TextView = itemView.findViewById(R.id.textDiscipline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_grade, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemName = disciplineList[position]
        val itemId = disciplineId[position]
        holder.textDiscipline.text = itemName
        holder.textDiscipline.setOnClickListener {
            openDisciplineActivity(itemId,itemName)
        }
    }

    override fun getItemCount(): Int {
        return disciplineList.size
    }
    private fun openDisciplineActivity(itemId:Int, title:String) {
        val intent: Intent = Intent(context, DisciplineActivity::class.java)
        intent.putExtra("id", itemId)
        intent.putExtra("title", title)
        context.startActivity(intent)
    }
}