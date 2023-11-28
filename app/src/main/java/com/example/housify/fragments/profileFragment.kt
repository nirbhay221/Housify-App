package com.example.housify.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.housify.LoginActivity
import com.example.housify.R
import com.example.housify.databinding.FragmentHomeBinding
import com.example.housify.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class profileFragment: Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutBtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
        }

    }

}