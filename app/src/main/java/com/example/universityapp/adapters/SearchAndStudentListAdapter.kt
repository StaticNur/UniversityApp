package com.example.universityapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.universityapp.R
import com.example.universityapp.data.db.userDao.UserRepository
import com.example.universityapp.data.entity.StudentDB

class SearchAndStudentListAdapter(
    private val recyclerView: RecyclerView,
    private val userRepository: UserRepository
) :
    RecyclerView.Adapter<SearchAndStudentListAdapter.ViewHolder>() {
    private var studentList: List<StudentDB> = ArrayList()
    private lateinit var onItemClick: OnItemStudentClicked

    fun setStudentList(studentList: List<StudentDB>) {
        this.studentList = studentList.reversed()
        notifyDataSetChanged()
        scrollToFirstItem()
    }

    private fun scrollToFirstItem() {
        recyclerView.post {
            recyclerView.scrollToPosition(0)
        }
    }

    fun onItemClicked(onItemClick: OnItemStudentClicked){
        this.onItemClick = onItemClick
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fio: TextView = itemView.findViewById(R.id.fioUserInItem)
        val ibAvatar: ImageView = itemView.findViewById(R.id.ivAvatarUserInItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutResId =  R.layout.list_item_search_user
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentList[position]
        holder.fio.text = student.FIO
        val imageUrl = student.Photo
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.ibAvatar)

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(student.Id.toString())
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    interface OnItemStudentClicked{
        fun onClickListener(id:String)
    }

    interface OnLongUserClick{
        fun onUserCLick(id:String)
    }
}