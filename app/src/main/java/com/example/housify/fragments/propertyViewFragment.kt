package com.example.housify.fragments

import android.content.Intent
import android.media.metrics.Event
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Adapter.propertyListAdapter
import com.example.housify.HomeActivity
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.databinding.FragmentPropertyAddBinding
import com.example.housify.databinding.FragmentPropertyViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class propertyViewFragment:Fragment(R.layout.fragment_property_view) {
    private lateinit var binding: FragmentPropertyViewBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var propertyArrayList: ArrayList<propertyModel>
    private lateinit var PropertyAdapter : propertyListAdapter
    private lateinit var fireStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyViewBinding.inflate(inflater)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.propertyRetrieveList
        recyclerView.layoutManager=LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        propertyArrayList= arrayListOf()
        PropertyAdapter= propertyListAdapter(propertyArrayList)
        recyclerView.adapter = PropertyAdapter
        binding.addPropertyPropertyViewFragment.setOnClickListener{
            findNavController().navigate(R.id.propertyAddFragment)
        }
        EventChangeListener()


    }
    private fun EventChangeListener(){
        fireStore = FirebaseFirestore.getInstance()
        var currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if(currentUserUid != null){
            fireStore.collection("Properties").whereEqualTo("userUid",currentUserUid)
                .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Log.e("Firestore Error",error.message.toString())
                        return
                    }

                    for(dc:DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            propertyArrayList.add(dc.document.toObject(propertyModel::class.java))

                        }

                    }
                    PropertyAdapter.notifyDataSetChanged()

                }
            })
        }


    }


    }