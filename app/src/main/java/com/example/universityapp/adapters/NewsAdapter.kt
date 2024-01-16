package com.example.universityapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.R
import com.example.universityapp.data.entity.NewsItem


class NewsAdapter() :
    RecyclerView.Adapter<NewsAdapter.AnswerViewHolder>() {
    private var newsList: List<NewsItem> = ArrayList()

    fun setNewsList(newsList: List<NewsItem>){
        this.newsList = newsList
        notifyDataSetChanged()
    }

    fun addNews(newsItem: NewsItem) {
        newsList = listOf(newsItem) + newsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer: NewsItem = newsList[position]
        holder.bind(answer)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
        private val webViewText: WebView = itemView.findViewById(R.id.webViewText)
        private val textViewHeader: TextView = itemView.findViewById(R.id.textViewHeader)

        fun bind(answer: NewsItem) {
            textViewDate.text = answer.Date
            if(answer.Header.isNullOrEmpty()){
                textViewHeader.visibility = View.INVISIBLE
            }else textViewHeader.text = answer.Header
            val htmlData =
                "<html><head><style>img {max-width: 100%; height: auto;}</style></head><body>" + answer.Text + "</body></html>"
            webViewText.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
        }
    }
}