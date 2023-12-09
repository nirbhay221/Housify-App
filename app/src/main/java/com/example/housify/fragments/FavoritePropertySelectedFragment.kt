package com.example.housify.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Adapter.propertyLikedFilterAdapter
import com.example.housify.Adapter.propertyListAdapter
import com.example.housify.Adapter.propertyPostedAdapter
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.databinding.FavoritePropertySelectedFragmentBinding
import com.example.housify.databinding.FragmentHomeBinding
import com.example.housify.databinding.FragmentPropertyAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class FavoritePropertySelectedFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding:FavoritePropertySelectedFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var propertyArrayList: ArrayList<propertyModel>
    private lateinit var PropertyLikedAdapter : propertyLikedFilterAdapter
    private lateinit var fireStore: FirebaseFirestore

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

        recyclerView = binding.propertyRetrieveLikedList
        recyclerView.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        propertyArrayList= arrayListOf()
        PropertyLikedAdapter= propertyLikedFilterAdapter(propertyArrayList)
        recyclerView.adapter = PropertyLikedAdapter
        binding.propertyRetrieveLikedList
        extractLikedProperties()

//        EventChangeListener()


    }

    private fun extractLikedProperties() {
        var currentUserUid= FirebaseAuth.getInstance().currentUser?.uid
        if(currentUserUid != null){
            FirebaseFirestore.getInstance().collection("User").document(currentUserUid)
                .get()
                .addOnSuccessListener {
                    documentSnapshot ->
                    var likedProperties = documentSnapshot["likedProperties"] as? List<String> ?: emptyList()
                    for(propertyUid in likedProperties){
                        fetchLikedPropertyDetails(propertyUid)
                    }
                }
                .addOnFailureListener { 
                    
                }

        }
    }

    private fun fetchLikedPropertyDetails(propertyUid: String) {
        FirebaseFirestore.getInstance().collection("Properties").document(propertyUid).get()
            .addOnSuccessListener { documentSnapshot ->
                var property = documentSnapshot.toObject(propertyModel::class.java)
                property?.let{
                    propertyArrayList.add(it)

                    PropertyLikedAdapter.notifyDataSetChanged()
                }
            }
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

                PropertyLikedAdapter.notifyDataSetChanged()
            }
        })

    }
}
