package com.example.housify.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Adapter.eventUserAdapter
import com.example.housify.R
import com.example.housify.UserModel
import com.example.housify.databinding.FragmentPersonalStatsBinding
import com.example.housify.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class personalStatsFragment : Fragment(R.layout.fragment_personal_stats) {
    private lateinit var binding: FragmentPersonalStatsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserModel>
    private lateinit var eventUserAdapter: eventUserAdapter
    private var userList:ArrayList<UserModel> = ArrayList()
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalStatsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        var currentUserUid= FirebaseAuth.getInstance().currentUser?.uid
        var eventUid = fireStore.generateUniqueId()
        var eventTitle = binding.eventTitle.text.toString()
        var eventDescription = binding.eventDescription.text.toString()
        var eventTransactionAmount = binding.eventTransactionAmount.text.toString()

        recyclerView = binding.userEventRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        userArrayList = arrayListOf()
        eventUserAdapter = eventUserAdapter(userArrayList)

        var selectedUserWithMoney = eventUserAdapter.getSelectedUsersWithMoney()
        recyclerView.adapter = eventUserAdapter


        fireStore.collection("Roommate")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                userArrayList.clear()

                for (document in value?.documents.orEmpty()) {
                    val roommatesArray = document.get("roommates") as? ArrayList<Map<String, Any>>

                    if (roommatesArray != null) {
                        for (roommate in roommatesArray) {
                            val uid = roommate["uid"] as? String
                            if (uid == currentUserUid) {
                                val roommateGroupId = document.getString("roommateGroupId")
                                val roommateGroupName = document.getString("roommateGroupName")
                                binding.eventRoommateGroupName.text =  Editable.Factory.getInstance().newEditable(roommateGroupName)
                                val updatedEvent = hashMapOf(
                                    "eventId" to eventUid,
                                    "eventTitle" to eventTitle,
                                    "eventDescription" to eventDescription,
                                    "eventTransactionAmount" to eventTransactionAmount,
                                    "assignedUsers" to selectedUserWithMoney,
                                    "roommateGroupUid" to roommateGroupId
                                )
                                if (isUserInSelectedRoommateGroup(roommateGroupId)) {
                                    fetchRoommatesFromGroup(roommateGroupId)
                                }
                                binding.addRoommateEventButton.setOnClickListener{
                                    val eventUid = fireStore.generateUniqueId()
                                    val eventTitle = binding.eventTitle.text.toString()
                                    val eventDescription = binding.eventDescription.text.toString()
                                    val eventTransactionAmount = binding.eventTransactionAmount.text.toString()
                                    val selectedUserWithMoney = eventUserAdapter.getSelectedUsersWithMoney()

                                    val updatedEvent = hashMapOf(
                                        "eventId" to eventUid,
                                        "eventTitle" to eventTitle,
                                        "eventDescription" to eventDescription,
                                        "eventTransactionAmount" to eventTransactionAmount,
                                        "assignedUsers" to selectedUserWithMoney,
                                        "roommateGroupUid" to roommateGroupId
                                    )

                                    if (isUserInSelectedRoommateGroup(roommateGroupId)) {
                                        fetchRoommatesFromGroup(roommateGroupId)
                                    }
                                    fireStore.collection("eventStorage")
                                        .document(eventUid)
                                        .set(updatedEvent)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Event added successfully to $eventTitle",
                                                Toast.LENGTH_LONG
                                            ).show()

                                        }.addOnFailureListener { e ->
                                            Toast.makeText(
                                                requireContext(),
                                                "Error adding event: ${e.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                            } else {
                                val userModel = UserModel(

                                    roommate["firstName"].toString() as String,
                                    roommate["lastName"].toString() as String,
                                    roommate["number"].toString() as String,
                                    roommate["userImage"].toString() as String,
                                    roommate["uid"].toString() as String

                                )
                                var userName = userModel.firstName
                                Toast.makeText(requireContext(),"$userName",Toast.LENGTH_LONG).show()
                                userArrayList.add(userModel)
                            }
                        }
                    }
                }
            }

        EventChangeListener()
    }

    private fun fetchRoommatesFromGroup(roommateGroupId: String?) {
        roommateGroupId?.let {
            fireStore.collection("Roommate")
                .whereEqualTo("roommateGroupId", roommateGroupId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return@addSnapshotListener
                    }

                    userArrayList.clear()

                    for (document in value?.documents.orEmpty()) {
                        val roommatesArray = document.get("roommates") as? ArrayList<Map<String, Any>>

                        if (roommatesArray != null) {
                            for (roommate in roommatesArray) {
                                val userModel = UserModel(
                                    roommate["uid"].toString() as String,
                                    roommate["firstName"].toString() as String,
                                    roommate["lastName"].toString() as String,
                                    roommate["number"].toString() as String,
                                    roommate["userImage"].toString() as String,
                                )
                                userArrayList.add(userModel)
                            }
                        }
                    }

                    if (::eventUserAdapter.isInitialized) {
                        eventUserAdapter.notifyDataSetChanged()
                    }
                }
        }
    }


    private fun isUserInSelectedRoommateGroup(selectedRoommateGroupId: String?): Boolean {

        return selectedRoommateGroupId != null && selectedRoommateGroupId.isNotEmpty()
    }

    private fun EventChangeListener() {
        var currentUserUid= FirebaseAuth.getInstance().currentUser?.uid
        fireStore.collection("Roommate")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                userArrayList.clear()

                for (document in value?.documents.orEmpty()) {
                    val roommatesArray = document.get("roommates") as? ArrayList<Map<String, Any>>

                    if (roommatesArray != null) {
                        for (roommate in roommatesArray) {
                            val uid = roommate["uid"] as? String
                            if (uid == currentUserUid) {
                                val roommateGroupId = document.getString("roommateGroupId")
                                val roommateGroupName = document.getString("roommateGroupName")
                                val userModel = UserModel(
                                    roommate["uid"].toString() as String,
                                    roommate["firstName"].toString() as String,
                                    roommate["lastName"].toString() as String,
                                    roommate["number"].toString() as String,
                                    roommate["userImage"].toString() as String,
                                )
                                val uid = roommate["uid"] as? String
                                val userUid = userModel.uid
                                Toast.makeText(requireContext(),"UID : $uid UserUid: $userUid",Toast.LENGTH_LONG).show()
                                userArrayList.add(userModel)

                                if (::eventUserAdapter.isInitialized) {
                                    eventUserAdapter.notifyDataSetChanged()
                                }
                    }
                        }
                    }
                }
            }


    }
    fun FirebaseFirestore.generateUniqueId(): String {
        return this.collection("dummyCollection").document().id
    }

}




