package com.example.universityapp.mvvm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universityapp.data.db.AppDatabase
import com.example.universityapp.data.db.newsDao.NewsRepository
import com.example.universityapp.data.entity.News
import com.example.universityapp.data.entity.NewsItem
import com.example.universityapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsFragMVVM(val token: String, val context: Context?) : ViewModel() {
    private val TAG: String = "NewsMVVM"
    private val mutableStudent:MutableLiveData<News> = MutableLiveData<News>()

    private  var newsRepository: NewsRepository
    private  var allNews: LiveData<List<NewsItem>>

    init {
        //getNews()
        val newsDao = AppDatabase.getInstance(context!!.applicationContext).newsDao()
        newsRepository = NewsRepository(newsDao)
        allNews = newsRepository.newsList
    }

    fun getNews() {
        RetrofitInstance.mrsuApi.getNews("Bearer $token")
            .enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            mutableStudent.value = response.body()
                        }
                    } else {
                        if (response.code() == 500) {
                            val errorNews = News()
                            errorNews.add(NewsItem(1000, "today", "<h2>error give new news</h2>", "<h1>Error server 500</h1>", true))
                            mutableStudent.value = errorNews
                        } else {
                            Log.e(TAG, "response code: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }
            })
    }

    fun observeNews(): LiveData<News> {
        return mutableStudent
    }

    fun getAllSavedNews(): LiveData<List<NewsItem>> {
        //viewModelScope.launch(Dispatchers.Main) {
          return allNews
       // }
    }

    fun insertNews(news: NewsItem) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.insertNews(news)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun deleteNews(news: NewsItem) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.deleteNews(news)
    }

    fun isNewsSavedInDatabase(id: String): Boolean {
        var news: NewsItem? = null
        runBlocking(Dispatchers.IO) {
            news = newsRepository.getNewsById(id)
        }
        if (news == null)
            return false
        return true
    }

    fun deleteNewsById(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deleteNewsById(id)
        }
    }

    fun deleteAllNews(){
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deleteAllNews()
        }
    }

}