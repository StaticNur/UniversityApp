package com.example.universityapp.hub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.databinding.FragmentHubBinding
import com.example.universityapp.hub.grade.GradeActivity


class HubFragment : Fragment() {
    private lateinit var binding: FragmentHubBinding
    private lateinit var viewModel: HubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HubViewModel::class.java)

        binding.grade.setOnClickListener{
            openGradeActivity()
        }
        binding.message.setOnClickListener {
            openMessageActivity()
        }
        binding.myIndividualTrajectory.setOnClickListener {
            openIndividualTrajectoryActivity()
        }
        NetworkTask(binding, this.requireContext()).execute()
    }

    private fun openIndividualTrajectoryActivity() {

    }

    private fun openMessageActivity() {


    }

    private fun openGradeActivity() {
        val intent:Intent = Intent(requireContext(), GradeActivity::class.java)
        startActivity(intent)
    }
}