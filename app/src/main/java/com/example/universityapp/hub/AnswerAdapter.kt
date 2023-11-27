package com.example.universityapp.hub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.model.news.News


class AnswerAdapter(private val answerList: List<News>) :
    RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer: News = answerList[position]
        holder.bind(answer)
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val webViewText: WebView = itemView.findViewById(R.id.webViewText)
        private val textViewHeader: TextView = itemView.findViewById(R.id.textViewHeader)

        fun bind(answer: News) {
            textViewDate.text = answer.date
            textViewHeader.text = answer.header

            // Используйте WebView для отображения HTML-разметки
            webViewText.loadDataWithBaseURL(null, answer.text.toString(), "text/html", "utf-8", null)
        }
    }
}