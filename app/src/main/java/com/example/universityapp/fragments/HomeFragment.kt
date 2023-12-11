package com.example.universityapp.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.auth.SignInActivity
import com.example.universityapp.databinding.FragmentHomeBinding
import com.example.universityapp.viewModels.HomeViewModel
import com.example.universityapp.home.NetworkHome


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sPref: SharedPreferences
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        initView()

    }
    fun initView(){
        setInfoAboutUser()
        val logout = binding.bLogout
        logout.setOnClickListener{
            openRunTimeSignInActivity()
        }
    }
    fun openRunTimeSignInActivity(){
        val intent: Intent = Intent(requireActivity(), SignInActivity::class.java)
        sPref.edit().putString("saved_token", "").commit()
        sPref.edit().putString("saved_refresh_token", "").commit()
        sPref.edit().putString("date_sign_in", "").commit()
        startActivity(intent)
    }
    fun setInfoAboutUser(){
        NetworkHome(binding, this.requireContext(), this).execute()
        //finish()
    }
}