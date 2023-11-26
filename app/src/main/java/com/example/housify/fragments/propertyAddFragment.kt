package com.example.housify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.databinding.FragmentPropertyAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class propertyAddFragment : Fragment(R.layout.fragment_property_add) {
    private lateinit var binding: FragmentPropertyAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val propertyTypes = arrayOf("House", "Apartment", "Condo", "Other")
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, propertyTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.propertyTypeInfo.adapter = spinnerAdapter

        binding.submitPropertyDetails.setOnClickListener {
            val propertyTitle = binding.propertyTitleInfo.text.toString()
            val propertyType = binding.propertyTypeInfo.selectedItem.toString()
            val propertyPrice = binding.propertyPriceInfo.text.toString()
            val propertyAddress = binding.propertyAddressInfo.text.toString()
            val propertyPasscode = binding.propertyPasscodeInfo.text.toString()
            val propertyDescription = binding.propertyDescriptionInfo.text.toString()

            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

            if (currentUserUid != null && propertyTitle.isNotEmpty() && propertyType.isNotEmpty() &&
                propertyPrice.isNotEmpty() && propertyAddress.isNotEmpty() &&
                propertyPasscode.isNotEmpty() && propertyDescription.isNotEmpty()
            ) {
                val propertyData = propertyModel(
                    propertyTitle,
                    propertyType,
                    propertyAddress,
                    propertyPasscode,
                    propertyPrice,
                    propertyDescription,
                    currentUserUid
                )

                val propertiesCollection =
                    FirebaseFirestore.getInstance().collection("Properties")

                propertiesCollection.add(propertyData)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(requireContext(), "PropertyAdded", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.propertyViewFragment)

                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "PropertyAdd Failure",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } else {
            }
        }
    }
}
