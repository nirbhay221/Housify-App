package com.example.housify.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.housify.Adapter.HomeFragmentDivisionAdapter
import com.example.housify.Adapter.propertyListAdapter
import com.example.housify.Adapter.propertyPostedAdapter
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.databinding.FragmentHomeBinding
import com.example.housify.databinding.FragmentPropertyAddBinding
import com.example.housify.searchFilterActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.Locale


class homeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var propertyArrayList: ArrayList<propertyModel>
    private lateinit var PropertyPostedAdapter : propertyPostedAdapter
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var homeFragmentDivisionAdapter: HomeFragmentDivisionAdapter
    private lateinit var nearbyProperties: nearbyPropertiesFragment
    private lateinit var popularProperties:popularPropertiesFragment
    private lateinit var allProperties:allPropertiesPostedFragment
    private lateinit var likedProperties: FavoritePropertySelectedFragment

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nearbyProperties = nearbyPropertiesFragment()
        popularProperties = popularPropertiesFragment()
        allProperties = allPropertiesPostedFragment()
        likedProperties = FavoritePropertySelectedFragment()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)
            .setFastestInterval(500)
        var currentUserUid = auth.currentUser?.uid
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation
                val address = lastLocation?.latitude?.let {
                    getAddressFromLocation(
                        it,
                        lastLocation.longitude
                    )
                }
                val editableAddress = Editable.Factory.getInstance().newEditable(address.toString())
                if (currentUserUid != null) {

                    val userCollection = FirebaseFirestore.getInstance().collection("User")
                    userCollection.document(currentUserUid).get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        userCollection.document(currentUserUid)
                            .update("userLocation", address.toString())
                            .addOnSuccessListener {
                                Log.d("User Location", "User location updated successfully")
                            }
                            .addOnFailureListener { exception ->
                                Log.e("User Location", "Error updating user location", exception)
                            }
                    } else {

                    }

            }
                }
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
//        binding.searchPageOpen.setOnClickListener{
//            var fragmentSearchFilter = searchFilterFragment()
//            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_in_home,fragmentSearchFilter)
//                .commit()
//        }
        binding.searchPageOpen.setOnClickListener {
            val intent = Intent(requireContext(), searchFilterActivity::class.java)
            startActivity(intent)
        }






        binding.saveUserCurrentLocation.setOnClickListener {

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
        setupViewPager(binding.viewPagerHome)
        binding.tabLayoutHome.setupWithViewPager(binding.viewPagerHome)
        binding.tabLayoutHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })


//        recyclerView = binding.propertyPostedRetrieveList
//        recyclerView.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        recyclerView.setHasFixedSize(true)
//        propertyArrayList= arrayListOf()
//        PropertyPostedAdapter= propertyPostedAdapter(propertyArrayList)
//        recyclerView.adapter = PropertyPostedAdapter
//        binding.propertyPostedRetrieveList
//
//
//        EventChangeListener()


    }

    private fun setupViewPager(viewPager: ViewPager) {

        homeFragmentDivisionAdapter = HomeFragmentDivisionAdapter(childFragmentManager)
        homeFragmentDivisionAdapter.addFragment(allProperties, "All")
        homeFragmentDivisionAdapter.addFragment(nearbyProperties, "Nearby")
        homeFragmentDivisionAdapter.addFragment(popularProperties, "Popular")
        homeFragmentDivisionAdapter.addFragment(likedProperties, "Liked")
        viewPager.adapter = homeFragmentDivisionAdapter
    }

    private fun EventChangeListener() {
        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("Properties").addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        propertyArrayList.add(dc.document.toObject(propertyModel::class.java))

                    }

                }
                PropertyPostedAdapter.notifyDataSetChanged()

            }
        })

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
