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
import com.example.universityapp.databinding.FragmentHubBinding
import com.example.universityapp.mvvm.NewsFragMVVM
import com.example.universityapp.ui.activities.DisciplineActivity
import com.example.universityapp.ui.activities.SecurityActivity
import com.example.universityapp.utils.NewsCustomFactory


class HubFragment : Fragment() {
    private lateinit var binding: FragmentHubBinding
    private lateinit var newsMvvm: NewsFragMVVM
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var sPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHubBinding.inflate(inflater, container, false)
        newsAdapter = NewsAdapter()
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsMvvm = ViewModelProvider(this,
            NewsCustomFactory(sPref.getString("saved_token", "").toString())
        )[NewsFragMVVM::class.java]

        prepareRecyclerView()
        observeNews()
        binding.grade.setOnClickListener{
            openGradeActivity()
        }
        binding.message.setOnClickListener {
            openMessageActivity()
        }
        binding.myIndividualTrajectory.setOnClickListener {
            openIndividualTrajectoryActivity()
        }
        binding.groupList.setOnClickListener {
            openGroupActivity()
        }
    }



    private fun observeNews() {
        newsMvvm.observeNews().observe(viewLifecycleOwner, object : Observer<News?> {
            override fun onChanged(t: News?) {
                if (!t!!.isEmpty()) {
                    newsAdapter.setNewsList(t)
                } else {
                    Toast.makeText(requireContext().applicationContext, "Не удалось получить новости", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun prepareRecyclerView() {
        binding.recyclerViewNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext()) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }
    private fun openIndividualTrajectoryActivity() {

    }

    private fun openGroupActivity() {
        val intent:Intent = Intent(requireContext(), SecurityActivity::class.java)
        startActivity(intent)
    }

    private fun openMessageActivity() {
        val intent:Intent = Intent(requireContext(), DisciplineActivity::class.java)
        intent.putExtra("button","message")
        startActivity(intent)
    }

    private fun openGradeActivity() {
        val intent:Intent = Intent(requireContext(), DisciplineActivity::class.java)
        intent.putExtra("button","grade")
        startActivity(intent)
    }
}