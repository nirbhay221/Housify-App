package com.example.housify.fragments

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
import com.example.housify.databinding.FragmentAllpropertiespostedBinding
import com.example.housify.databinding.FragmentFilteredPropertiesPostedBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class filteredPropertiesPostedFragment: Fragment(R.layout.fragment_filtered_properties_posted) {
    private lateinit var binding: FragmentFilteredPropertiesPostedBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var propertyArrayList: ArrayList<propertyModel>
    private lateinit var PropertyPostedAdapter : propertyPostedAdapter
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var homeFragmentDivisionAdapter: HomeFragmentDivisionAdapter
    private lateinit var nearbyProperties: nearbyPropertiesFragment
    private lateinit var popularProperties:popularPropertiesFragment

    private var selectedPropertyTypes: List<String>? = null
    private var selectedPropertyMoneyTypes: List<String>? = null
    private var selectedPropertyFacilities: List<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilteredPropertiesPostedBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.filteredPropertyPostedRetrieveList
        recyclerView.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        propertyArrayList= arrayListOf()
        PropertyPostedAdapter= propertyPostedAdapter(propertyArrayList)
        recyclerView.adapter = PropertyPostedAdapter
        binding.filteredPropertyPostedRetrieveList
        arguments?.let {
            selectedPropertyTypes = it.getStringArrayList("selectedPropertyTypes")
            selectedPropertyMoneyTypes = it.getStringArrayList("selectedPropertyMoneyTypes")
            selectedPropertyFacilities = it.getStringArrayList("selectedPropertyFacilities")
        }

        EventChangeListener()

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
                        val property = dc.document.toObject(propertyModel::class.java)
                        if(isPropertyMatch(property)){
                            propertyArrayList.add(property)
                        }
                        
                    }

                    PropertyPostedAdapter.notifyDataSetChanged()
                }

            }
        })

    }

    private fun isPropertyMatch(property: propertyModel): Boolean {
        if(selectedPropertyTypes!= null && !selectedPropertyTypes!!.contains(property.propertyType))
        {
            return false

        }
        return true
    }

}