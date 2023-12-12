package com.example.housify.fragments

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Adapter.HomeFragmentDivisionAdapter
import com.example.housify.Adapter.propertyPostedAdapter
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.UserModel
import com.example.housify.databinding.FragmentAllpropertiespostedBinding
import com.example.housify.databinding.FragmentChatBinding
import com.example.housify.databinding.FragmentNearbyPropertiesBinding
import com.example.housify.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.tomtom.sdk.search.SearchCallback
import com.tomtom.sdk.search.SearchOptions
import com.tomtom.sdk.search.SearchResponse
import com.tomtom.sdk.search.common.error.SearchFailure
import com.tomtom.sdk.search.online.OnlineSearch
import kotlin.properties.Delegates

class nearbyPropertiesFragment: Fragment(R.layout.fragment_nearby_properties) {
    private lateinit var binding: FragmentNearbyPropertiesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var propertyArrayList: ArrayList<propertyModel>
    private lateinit var PropertyPostedAdapter : propertyPostedAdapter
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var homeFragmentDivisionAdapter: HomeFragmentDivisionAdapter
    private lateinit var nearbyProperties: nearbyPropertiesFragment
    private lateinit var popularProperties:popularPropertiesFragment
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private var userLongitude by Delegates.notNull<Double>()
    private var userLatitude by Delegates.notNull<Double>()

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
        val searchApi = OnlineSearch.create(requireContext(), "eXRlAZJos3TBi0kr7fSrXrp8Kl7Nt1e8")

        recyclerView = binding.propertyNearbyRetrievedList
        recyclerView.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        propertyArrayList= arrayListOf()
        PropertyPostedAdapter= propertyPostedAdapter(propertyArrayList)
        recyclerView.adapter = PropertyPostedAdapter
        binding.propertyNearbyRetrievedList

        var currentUserUid= auth.currentUser?.uid

        var userCurrentLocation = "somewhere"
        val userCollection = FirebaseFirestore.getInstance().collection("User")
        if (currentUserUid != null) {
            userCollection.document(currentUserUid).addSnapshotListener{ documentSnapshot, error ->
                if(error != null){
                    return@addSnapshotListener
                }
                if(documentSnapshot != null && documentSnapshot.exists()){
                    var user= documentSnapshot.toObject(UserModel::class.java)
                    if(user!= null && user.userLocation != null){
                        userCurrentLocation = user.userLocation.toString()
                    }
                }
            }
        }

        val options = userCurrentLocation?.let { SearchOptions(query = it, limit = 1) }

        options?.let {
            searchApi.search(it, object : SearchCallback {

                override fun onSuccess(result: SearchResponse) {
                    if (result.results.isNotEmpty()) {
                        val firstResult = result.results.first()
                        val place = firstResult.place

                        if (place != null && place.coordinate != null) {
                             userLatitude = place.coordinate.latitude
                             userLongitude = place.coordinate.longitude


                            Log.d("TomTom", "Latitude: $userLatitude, Longitude: $userLongitude")

                            EventChangeListener()
                        }      } else {

                    }
                }

                override fun onFailure(failure: SearchFailure) {
                }
            })
        }


    }
    private fun EventChangeListener() {
        fireStore = FirebaseFirestore.getInstance()
        val searchApi = OnlineSearch.create(requireContext(), "eXRlAZJos3TBi0kr7fSrXrp8Kl7Nt1e8")

        fireStore.collection("Properties").addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                var propertiesList: MutableList<Pair<propertyModel,Double>> = mutableListOf()
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val property = dc.document.toObject(propertyModel::class.java)
                        var propertyLocation = property.propertyAddress
                        val options = propertyLocation?.let { SearchOptions(query = it, limit = 1) }
                        options?.let {
                            searchApi.search(it,object: SearchCallback{
                                override fun onFailure(failure: SearchFailure) {

                                }

                                override fun onSuccess(result: SearchResponse) {
                                    if(result.results.isNotEmpty()){
                                        val firstResult = result.results.first()
                                        val place = firstResult.place
                                        if(place != null && place.coordinate != null){
                                            val propertyLatitude = place.coordinate.latitude
                                            val propertyLongitude = place.coordinate.longitude
                                            val distance = calculateDistance(property.propertyTitle,userLatitude?:0.0,userLongitude?:0.0,propertyLatitude,propertyLongitude)
                                            propertiesList.add(Pair(property,distance))
                                            propertiesList.sortBy { it.second }
                                            propertyArrayList.clear()
                                            propertyArrayList.addAll(propertiesList.map { it.first })
                                            PropertyPostedAdapter.notifyDataSetChanged()
                                        }
                                    }
                                }

                            })
                        }
//                        propertyArrayList.add(dc.document.toObject(propertyModel::class.java))


                    }

                }

            }
        })

    }
    private fun calculateDistance(
        propertyTitle: String?,
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val locationA = Location("point A")
        locationA.latitude = lat1
        locationA.longitude = lon1

        val locationB = Location("point B")
        locationB.latitude = lat2
        locationB.longitude = lon2

        val distance = locationA.distanceTo(locationB) / 1000
        Log.d("User Distance Calculation", "Latitude1: $lat1, Longitude1: $lon1")
        Log.d("Property Distance Calculation", "Latitude2: $lat2, Longitude2: $lon2 Property Name: $propertyTitle")
        Log.d("Distance Calculation", "Distance: $distance km")
        return distance.toDouble()
    }

}