package com.example.universityapp.adapters

import android.content.SharedPreferences
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.universityapp.R
import com.example.universityapp.data.db.userDao.UserRepository
import com.example.universityapp.data.entity.MessageItem
import com.example.universityapp.data.entity.StudentDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MessageAdapter(
    private val recyclerView: RecyclerView,
    private val userRepository: UserRepository
) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private var messageList: List<MessageItem> = ArrayList()
    private lateinit var onItemClick: OnItemUserClicked
    private lateinit var sPref: SharedPreferences

    fun setMessageList(messageList: List<MessageItem>, sPref: SharedPreferences) {
        this.messageList = messageList.reversed()
        this.sPref = sPref
        notifyDataSetChanged()
        scrollToLastItem()
    }

    private fun scrollToLastItem() {
        recyclerView.post {
            recyclerView.scrollToPosition(itemCount - 1)
        }
    }

    fun onItemClicked(onItemClick: OnItemUserClicked) {
        this.onItemClick = onItemClick
    }

    fun addMessage(messageItem: MessageItem) {
        println("addMessage ${messageItem.toString()}")
        messageList = messageList + listOf(messageItem)
        notifyDataSetChanged()
        scrollToLastItem()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.textMessage)
        val messageDate: TextView = itemView.findViewById(R.id.dateMessage)
        val fio: TextView = itemView.findViewById(R.id.fio)
        val messageLayout: LinearLayout = itemView.findViewById(R.id.messageLayout)
        val ibAvatar: ImageButton = itemView.findViewById(R.id.ib_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutResId = if (viewType == LEFT_MESSAGE_TYPE) {
            R.layout.list_item_message_left
        } else {
            R.layout.list_item_message_right
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageItem = messageList[position]
        holder.messageText.text = messageItem.Text
        holder.messageDate.text = messageItem.CreateDate
        holder.fio.text = messageItem.User?.FIO
        val imageUrl = messageItem.User?.Photo?.UrlSmall.toString()
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .circleCrop()
            .into(holder.ibAvatar)
        if (getItemViewType(position) == RIGHT_MESSAGE_TYPE) {
            holder.messageLayout.gravity = Gravity.END  // Сообщение справа
        } else {
            holder.messageLayout.gravity = Gravity.START  // Сообщение слева
        }

        holder.ibAvatar.setOnClickListener {
            onItemClick.onClickListener(messageItem.User!!.Id)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = if (isMyMessage(messageList[position])) RIGHT_MESSAGE_TYPE
        else LEFT_MESSAGE_TYPE
        return viewType
    }

    private fun isMyMessage(message: MessageItem): Boolean {
        return runBlocking {
            isMyMessageSuspend(message)
        }
    }

    private suspend fun isMyMessageSuspend(message: MessageItem): Boolean {
        val userId = message.User?.Id
        println("userId: $userId")
        val maybeIsMe: StudentDB? = withContext(Dispatchers.IO) {
            try {
                userRepository.getUserById(userId!!)
            } catch (e: Exception) {
                null
            }
        }
        val myId = sPref.getString("my_id", "").toString()
        if ((maybeIsMe != null) && (myId == maybeIsMe.Id)) {
            println("userId: $userId this is me")
            return true
        }
        return false
    }

    interface OnItemUserClicked {
        fun onClickListener(id: String)
    }

    interface OnLongUserClick {
        fun onUserCLick(id: String)
    }

    companion object {
        private const val RIGHT_MESSAGE_TYPE = 1
        private const val LEFT_MESSAGE_TYPE = 2
    }
}