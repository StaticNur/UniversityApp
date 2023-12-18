package com.example.universityapp.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.universityapp.R
import com.example.universityapp.data.entity.Student
import com.example.universityapp.databinding.FragmentHomeBinding
import com.example.universityapp.mvvm.HomeFragMVVM
import com.example.universityapp.ui.activities.SignInActivity
import com.example.universityapp.utils.HomeCustomFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainFragMVVM = ViewModelProvider(this,
            HomeCustomFactory(sPref.getString("saved_token", "").toString()))[HomeFragMVVM::class.java]
        showLoadingCase()

        mainFragMVVM.observeRandomMeal().observe(viewLifecycleOwner, object : Observer<Student> {
            override fun onChanged(value: Student) {
                if(value != null){
                    val mealImage = binding.ivAvatar
                    val imageUrl = value.Photo?.UrlSmall.toString()
                    val fIO = value.FIO
                    Glide.with(this@HomeFragment)
                        .load(imageUrl)
                        .circleCrop()
                        .into(mealImage)
                    binding.tvNameStudent.text = fIO.toString()
                    cancelLoadingCase()
                }else{
                    Toast.makeText(context, "Не актуальный токен или проблема с сервером. Уже не то)", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.bLogout.setOnClickListener{
            openRunTimeSignInActivity()
        }
    }

    private fun showLoadingCase() {
        binding.apply {
            ivPhotoMgu.visibility = View.INVISIBLE
            background.visibility = View.INVISIBLE
            items.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_loading))
        }
    }

    private fun cancelLoadingCase() {
        binding.apply {
            ivPhotoMgu.visibility = View.VISIBLE
            background.visibility = View.VISIBLE
            items.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun openRunTimeSignInActivity(){
        val intent: Intent = Intent(requireActivity(), SignInActivity::class.java)
        sPref.edit().putString("saved_token", "").commit()
        sPref.edit().putString("saved_refresh_token", "").commit()
        sPref.edit().putString("date_sign_in", "").commit()
        startActivity(intent)
    }
}