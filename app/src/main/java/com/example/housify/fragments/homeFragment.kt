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
import android.widget.TableLayout
import android.widget.TextView
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
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class homeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var propertyArrayList: ArrayList<propertyModel>
    private lateinit var PropertyPostedAdapter : propertyPostedAdapter
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var homeFragmentDivisionAdapter: HomeFragmentDivisionAdapter
    private lateinit var nearbyProperties: nearbyPropertiesFragment
    private lateinit var popularProperties:popularPropertiesFragment

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
        nearbyProperties =nearbyPropertiesFragment()
        popularProperties = popularPropertiesFragment()
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
        homeFragmentDivisionAdapter.addFragment(nearbyProperties, "Nearby")
        homeFragmentDivisionAdapter.addFragment(popularProperties, "Popular")
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
}
