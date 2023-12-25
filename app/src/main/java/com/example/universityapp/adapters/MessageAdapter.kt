package com.example.universityapp.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.data.entity.MessageItem

class MessageAdapter() : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private var messageList: List<MessageItem> = ArrayList()

    fun setMessageList(messageList: List<MessageItem>){
        this.messageList = messageList
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.textMessage)
        val messageDate: TextView = itemView.findViewById(R.id.dateMessage)
        val messageLayout: LinearLayout = itemView.findViewById(R.id.messageLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutResId = if (viewType == RIGHT_MESSAGE_TYPE) {
            R.layout.list_item_message_right
        } else {
            R.layout.list_item_message_left
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageItem = messageList[position]
        holder.messageText.text = messageItem.Text
        holder.messageDate.text = messageItem.CreateDate

        // Дополнительные настройки для определения положения сообщения
        if (getItemViewType(position) == RIGHT_MESSAGE_TYPE) {
            holder.messageLayout.gravity = Gravity.END  // Сообщение справа
        } else {
            holder.messageLayout.gravity = Gravity.START  // Сообщение слева
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        // Возвращаем тип элемента (правый или левый)
        return LEFT_MESSAGE_TYPE
    }

    companion object {
        private const val RIGHT_MESSAGE_TYPE = 1
        private const val LEFT_MESSAGE_TYPE = 2
    }
}