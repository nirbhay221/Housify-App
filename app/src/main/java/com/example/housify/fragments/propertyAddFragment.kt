package com.example.housify.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.databinding.FragmentPropertyAddBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class propertyAddFragment : Fragment(R.layout.fragment_property_add) {
    private lateinit var binding: FragmentPropertyAddBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)
            .setFastestInterval(500)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation
                val address = lastLocation?.latitude?.let { getAddressFromLocation(it, lastLocation.longitude) }

                val editableAddress = Editable.Factory.getInstance().newEditable(address.toString())

                binding.propertyAddressInfo.text= editableAddress
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }


        val propertyTypes = arrayOf("House", "Apartment", "Condo", "Other")
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, propertyTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.propertyTypeInfo.adapter = spinnerAdapter

        binding.getCurrentLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            } else {
                requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
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
    @SuppressLint("MissingPermission")
    val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                }
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): Any {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            return addresses.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
        }
        return 0

    }
}
