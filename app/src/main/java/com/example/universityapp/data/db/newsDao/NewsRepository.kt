package com.example.universityapp.data.db.newsDao

import androidx.lifecycle.LiveData
import com.example.universityapp.data.entity.NewsItem

class NewsRepository(private val newsDao: NewsDao) {

    val newsList: LiveData<List<NewsItem>> = newsDao.getAllSavedNews()

    suspend fun insertNews(user: NewsItem) {
        newsDao.insertNews(user)
    }

    suspend fun getNewsById(id: String): NewsItem? {
        return newsDao.getNewsById(id)
    }

    suspend fun deleteNewsById(id: String) {
        newsDao.deleteNewsById(id)
    }

    suspend fun deleteNews(news: NewsItem) = newsDao.deleteNews(news)

    suspend fun deleteAllNews() = newsDao.deleteAllNews()
}