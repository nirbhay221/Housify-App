package com.example.housify.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap

import android.graphics.BitmapRegionDecoder
import android.location.Geocoder
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
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
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.tomtom.quantity.Distance
import com.tomtom.sdk.search.autocomplete.AutocompleteCallback
import com.tomtom.sdk.search.autocomplete.AutocompleteOptions
import com.tomtom.sdk.search.autocomplete.AutocompleteResponse
import com.tomtom.sdk.search.common.error.SearchFailure
import com.tomtom.sdk.search.model.result.AutocompleteResult
import com.tomtom.sdk.search.model.result.AutocompleteSegmentPoiCategory
import com.tomtom.sdk.search.online.OnlineSearch
import java.util.Locale

class propertyAddFragment : Fragment(R.layout.fragment_property_add) {
    private lateinit var binding: FragmentPropertyAddBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var propertyImage: ImageView?=null
    private lateinit var propertyImageUploadButton:Button
    private lateinit var propertyImageCameraButton:Button
    private var uri : Uri? = null
    private var CAMERA_PHOTO = 1
    private var FOLDER_PHOTO = 2
    private val CAMERA_CODE_REQ = 10
    private var propertyImageBase64Converted: String ?= null
    private val UPLOAD_IMAGE_CODE_REQ = 20
    private lateinit var searchApi: OnlineSearch
    val cameraActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data: Intent ?= result.data
            handleImageResult(data)
        }
    }
    val galleryActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data: Intent ?= result.data
            handleGalleryImageResult(data)
        }
    }
    fun handleImageResult(data:Intent?){
        if(data!= null){
            if(data.data != null){
                uri = data.data
            }
            else if(data.extras?.get("data") != null){
                val bitmap = data.extras?.get("data") as Bitmap?
                binding.propertyProfileImage?.setImageBitmap(bitmap)
                propertyImageBase64Converted = bitmap?.let{
                    encodeBitmapToBase64(it)
                }

            }
        }
    }
    fun handleGalleryImageResult(data: Intent?) {
        if (data != null) {
            if (data.clipData != null) {
                for (i in 0 until data.clipData!!.itemCount) {
                    val uri = data.clipData!!.getItemAt(i).uri
                    handleGalleryImage(uri)
                }
            } else if (data.data != null) {
                val uri = data.data
                handleGalleryImage(uri)
            }
        }
    }

    fun handleGalleryImage(uri: Uri?) {
        if (uri != null) {
            binding.propertyProfileImage?.setImageURI(uri)
            propertyImageBase64Converted = uri.let {
                encodeUriToBase64(it)
            }
        }
    }
    private fun encodeUriToBase64(uri: Uri): String {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()
            return byteArray?.let { Base64.encodeToString(it, Base64.DEFAULT) } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "${e}", Toast.LENGTH_LONG).show()
            return ""
        }
    }

    private fun encodeBitmapToBase64(it: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

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
        val searchApi = context?.let { OnlineSearch.create(it, "eXRlAZJos3TBi0kr7fSrXrp8Kl7Nt1e8") }
        val autoCompleteCallback = object :AutocompleteCallback{
            override fun onSuccess(result: AutocompleteResponse) {
                val predictionResult: List<AutocompleteResult>  = result.results
                Toast.makeText(requireContext(),"$predictionResult",Toast.LENGTH_LONG).show()
            }

            override fun onFailure(failure: SearchFailure) {
                Log.e("AutocompleteFailure", "Error: $failure")
            }
        }
        binding.propertyAddressInfo.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val querySearch  =  p0.toString()
                val amsterdam = com.tomtom.sdk.location.GeoPoint(52.379189,4.899431)
                val autoCompleteOptions = com.tomtom.sdk.search.autocomplete.AutocompleteOptions(
                    query = querySearch,
                    locale = Locale("en","US"),
                    position = amsterdam,
                    radius = Distance.meters(555600000 )
                )
                searchApi?.autocompleteSearch(autoCompleteOptions,object:AutocompleteCallback{
                    override fun onSuccess(result: AutocompleteResponse) {
                        val predictionResult: List<AutocompleteResult> = result.results
                        val firstPrediction = predictionResult[0]
                        val predictedAddress = firstPrediction.segments.firstOrNull{
                            it is AutocompleteSegmentPoiCategory
                        } as AutocompleteSegmentPoiCategory?
                        val address = predictedAddress?.poiCategory?.name?:"No Suggestions"
                        Toast.makeText(requireContext(),"$address",Toast.LENGTH_LONG).show()
                        Log.e("AutoComplete Result", "Result Success : $predictionResult")
                    }

                    override fun onFailure(failure: SearchFailure) {
                        Log.e("AutoComplete Failure", "Error: $failure")
                    }
                })
                Toast.makeText(requireContext(),"Text Changed",Toast.LENGTH_LONG).show()
            }
        })
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
        binding.propertyImageCaptureButton.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraActivityResult.launch(intent)
        }
        binding.propertyImageUploadButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            galleryActivityResult.launch(intent)
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
            Toast.makeText(requireContext(),"$currentUserUid",Toast.LENGTH_LONG).show()

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

                if (propertyImageBase64Converted != null) {
                    propertyData.userPropertyImages = propertyImageBase64Converted
                }

                val propertiesCollection =
                    FirebaseFirestore.getInstance().collection("Properties")

                propertiesCollection.add(propertyData)
                    .addOnSuccessListener { documentReference ->
                        var generatedUid = documentReference.id
                        propertyData.propertyUid = generatedUid
                        documentReference.update("propertyUid",generatedUid).addOnSuccessListener {
                            Toast.makeText(requireContext(), "PropertyAdded", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.propertyViewFragment)
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "PropertyAdd Failure",
                                Toast.LENGTH_LONG
                            ).show()
                            documentReference.update(
                                "userUid",
                                FirebaseAuth.getInstance().currentUser?.uid
                            ).addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "PropertyUserUidAdded",
                                    Toast.LENGTH_LONG
                                ).show()
                            }.addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    "PropertyAdd Failure",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            Toast.makeText(requireContext(), "PropertyAdded", Toast.LENGTH_LONG)
                                .show()
                        }
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