package com.example.housify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Adapter.eventListViewAdapter
import com.example.housify.Adapter.taskPostedAdapter
import com.example.housify.Models.TaskModel
import com.example.housify.Models.eventModel
import com.example.housify.R
import com.example.housify.databinding.FragmentEventListViewBinding
import com.example.housify.databinding.FragmentExpensesBinding
import com.example.housify.databinding.FragmentPersonalStatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class eventListViewFragment: Fragment(R.layout.fragment_event_list_view) {
    private lateinit var binding: FragmentEventListViewBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventList: ArrayList<eventModel>
    private lateinit var eventAdapter: eventListViewAdapter
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventListViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.eventPostedList
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        eventList = arrayListOf()
        eventAdapter = eventListViewAdapter(eventList)
        recyclerView.adapter = eventAdapter

        EventChangeListener()
    }
    private fun EventChangeListener() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            fireStore.collection("Roommate")
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(
                            requireContext(),
                            "No roommate groups found",
                            Toast.LENGTH_LONG
                        ).show()
                        return@addOnSuccessListener
                    }
                    for (document in documents) {
                        val roommatesArray =
                            document.get("roommates") as? ArrayList<Map<String, Any>>

                        if (roommatesArray != null) {
                            for (roommate in roommatesArray) {
                                val uid = roommate["uid"] as? String
                                if (uid == currentUserUid) {

                                    Toast.makeText(
                                        requireContext(),
                                        "User found in a roommate group",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    val roommateGroupId = document.getString("roommateGroupId")
                                    if (roommateGroupId != null) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Roommate group ID: $roommateGroupId",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        retrieveEventsForRoommate(roommateGroupId)
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Current user UID is null",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun retrieveEventsForRoommate(roommateGroupId: String) {
        fireStore.collection("eventStorage")
            .whereEqualTo("roommateGroupUid", roommateGroupId)
            .get()
            .addOnSuccessListener { eventDocuments ->
                eventList.clear()

                for (eventDocument in eventDocuments) {
                    val eventId = eventDocument.getString("eventId")
                    val eventTitle = eventDocument.getString("eventTitle")
                    val eventDescription = eventDocument.getString("eventDescription")
                    val eventTransactionAmount = eventDocument.getString("eventTransactionAmount")
                    val roommateId= eventDocument.getString("roommateGroupUid")
                    if (eventId != null && eventTitle != null && eventDescription != null ) {
                        val taskModel = eventModel(
                            eventId,
                            eventTitle,
                            eventDescription,
                            roommateGroupId,
                            eventTransactionAmount
                        )
                        eventList.add(taskModel)
                    }
                }

                eventAdapter.notifyDataSetChanged()

                Toast.makeText(
                    requireContext(),
                    "Event for roommate group $roommateGroupId retrieved successfully",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error getting Event: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}