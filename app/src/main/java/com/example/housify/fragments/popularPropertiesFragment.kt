package com.example.housify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.housify.R
import com.example.housify.databinding.FragmentChatBinding
import com.example.housify.databinding.FragmentPopularPropertiesBinding

class popularPropertiesFragment : Fragment(R.layout.fragment_popular_properties) {
    private lateinit var binding: FragmentPopularPropertiesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPopularPropertiesBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}