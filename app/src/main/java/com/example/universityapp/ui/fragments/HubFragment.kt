package com.example.universityapp.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.adapters.NewsAdapter
import com.example.universityapp.data.entity.News
import com.example.universityapp.data.entity.NewsItem
import com.example.universityapp.databinding.FragmentHubBinding
import com.example.universityapp.mvvm.NewsFragMVVM
import com.example.universityapp.ui.activities.DisciplineActivity
import com.example.universityapp.ui.activities.SearchAndStudentListActivity
import com.example.universityapp.ui.activities.SecurityActivity
import com.example.universityapp.utils.NewsCustomFactory


class HubFragment : Fragment() {
    private lateinit var binding: FragmentHubBinding
    private lateinit var newsMvvm: NewsFragMVVM
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var sPref: SharedPreferences
    private var newsInDBSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        newsMvvm = ViewModelProvider(
            requireActivity(),
            NewsCustomFactory(sPref.getString("saved_token", "").toString(), context)
        )[NewsFragMVVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHubBinding.inflate(inflater, container, false)
        newsAdapter = NewsAdapter()
        //sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        showLoadingCase()
        newsMvvm.getAllSavedNews().observe(viewLifecycleOwner) { newsInDB ->
            newsInDBSize = newsInDB.size
            newsAdapter.setNewsList(newsInDB.reversed())
            newsMvvm.getNews()
            observeNews()
            cancelLoadingCase()
        }

        binding.grade.setOnClickListener {
            openGradeActivity()
        }

        binding.message.setOnClickListener {
            openMessageActivity()
        }

        binding.bStudentList.setOnClickListener {
            openSearchAndStudentListActivity()
        }

        binding.groupList.setOnClickListener {
            openGroupActivity()
        }
    }

    private fun observeNews() {
        newsMvvm.observeNews().observe(viewLifecycleOwner, object : Observer<News?> {
            override fun onChanged(n: News?) {
                if (!n!!.isNullOrEmpty()) {
                    val beforeSize = newsInDBSize
                    val afterSize = n.size
                    var newNewsPublishedCount = afterSize - beforeSize
                    if(afterSize == 1){ //получили 500 ошибку
                        val newNewsPublished = n[0]
                        newsAdapter.addNews(newNewsPublished)
                    }else{
                        println("newNewsPublishedCount: $newNewsPublishedCount")
                        if (newNewsPublishedCount != 0) {
                            for (i in 1..newNewsPublishedCount) {
                                val newNewsPublished = n[newNewsPublishedCount - i]
                                saveNews(newNewsPublished)
                                newsAdapter.addNews(newNewsPublished)
                            }
                        }
                    }

                } else {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Не удалось получить новости",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun showLoadingCase() {
        binding.loadingGif.visibility = View.VISIBLE
    }

    fun cancelLoadingCase() {
        binding.loadingGif.visibility = View.INVISIBLE
    }

    private fun saveAllNews(newsList: News) {
        for (newsItem in newsList) {
            saveNews(newsItem)
        }
    }

    private fun prepareRecyclerView() {
        binding.recyclerViewNews.apply {
            adapter = newsAdapter
            layoutManager =
                LinearLayoutManager(requireContext()) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }

    private fun openSearchAndStudentListActivity() {
        val intent: Intent = Intent(requireContext(), SearchAndStudentListActivity::class.java)
        startActivity(intent)
    }

    private fun openGroupActivity() {
        val intent: Intent = Intent(requireContext(), SecurityActivity::class.java)
        startActivity(intent)
    }

    private fun openMessageActivity() {
        val intent: Intent = Intent(requireContext(), DisciplineActivity::class.java)
        intent.putExtra("button", "message")
        startActivity(intent)
    }

    private fun openGradeActivity() {
        val intent: Intent = Intent(requireContext(), DisciplineActivity::class.java)
        intent.putExtra("button", "grade")
        startActivity(intent)
    }


    private fun deleteAllNews() {
        newsMvvm.deleteAllNews()
    }

    private fun saveNews(news: NewsItem) {
        newsMvvm.insertNews(news)
    }
}