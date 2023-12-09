package com.example.housify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.housify.R
import com.example.housify.databinding.FragmentChatBinding
import com.example.housify.databinding.FragmentNearbyPropertiesBinding

class nearbyPropertiesFragment : Fragment(R.layout.fragment_nearby_properties) {
    private lateinit var binding: FragmentNearbyPropertiesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNearbyPropertiesBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}