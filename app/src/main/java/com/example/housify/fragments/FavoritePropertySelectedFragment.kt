package com.example.housify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.Fragment
import com.example.housify.R
import com.example.housify.databinding.FavoritePropertySelectedFragmentBinding
import com.example.housify.databinding.FragmentHomeBinding

class FavoritePropertySelectedFragment: Fragment(R.layout.favorite_property_selected_fragment) {
    private lateinit var binding: FavoritePropertySelectedFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoritePropertySelectedFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}