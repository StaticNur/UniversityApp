package com.example.universityapp.hub.message

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R

class MessageAdapter(private val messageItems: List<MessageItem>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

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
        val messageItem = messageItems[position]
        holder.messageText.text = messageItem.messageText
        holder.messageDate.text = messageItem.messageDate.toString()

        // Дополнительные настройки для определения положения сообщения
        if (getItemViewType(position) == RIGHT_MESSAGE_TYPE) {
            holder.messageLayout.gravity = Gravity.END  // Сообщение справа
        } else {
            holder.messageLayout.gravity = Gravity.START  // Сообщение слева
        }
    }

    override fun getItemCount(): Int {
        return messageItems.size
    }

    override fun getItemViewType(position: Int): Int {
        // Возвращаем тип элемента (правый или левый)
        messageItems[3].isRightMessage = true
        messageItems[5].isRightMessage = true
        messageItems[7].isRightMessage = true
        return if (messageItems[position].isRightMessage) RIGHT_MESSAGE_TYPE else LEFT_MESSAGE_TYPE
    }

    companion object {
        private const val RIGHT_MESSAGE_TYPE = 1
        private const val LEFT_MESSAGE_TYPE = 2
    }
}