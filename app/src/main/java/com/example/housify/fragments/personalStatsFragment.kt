package com.example.housify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.housify.R
import com.example.housify.databinding.FragmentExpensesBinding
import com.example.housify.databinding.FragmentPersonalStatsBinding

class personalStatsFragment : Fragment(R.layout.fragment_personal_stats) {
    private lateinit var binding: FragmentPersonalStatsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalStatsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}