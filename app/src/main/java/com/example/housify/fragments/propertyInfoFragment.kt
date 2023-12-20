package com.example.housify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.housify.R
import com.example.housify.databinding.FragmentProfileBinding
import com.example.housify.databinding.PropertyInfoFragmentBinding

// fragment for information about property selected

class propertyInfoFragment : Fragment(R.layout.property_info_fragment) {
    private lateinit var binding: PropertyInfoFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PropertyInfoFragmentBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val propertyName = arguments?.getString("propertyName")
//        val propertyLocation = arguments?.getString("propertyLocation")
//        val propertyUid = arguments?.getString("propertyUid")
//        binding.propertyNameInfo.text = propertyName
//        binding.propertyLocationInfo.text = propertyLocation
//        binding.propertyUidInfo.text = propertyUid
//

    }

}