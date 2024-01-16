package com.example.universityapp.data.db.newsDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.universityapp.data.entity.NewsItem

@Dao
interface NewsDao {
    @Insert
    fun insertNews(news: NewsItem)

    @Update
    fun updateNews(news:NewsItem)

    @Query("SELECT * FROM news order by id asc")
    fun getAllSavedNews():LiveData<List<NewsItem>>

    @Query("SELECT * FROM news WHERE id =:id")
    fun getNewsById(id:String):NewsItem?

    @Query("DELETE FROM news WHERE id =:id")
    fun deleteNewsById(id:String)

    @Query("DELETE FROM news WHERE 1 = 1")
    fun deleteAllNews()

    @Delete
    fun deleteNews(news:NewsItem)
}