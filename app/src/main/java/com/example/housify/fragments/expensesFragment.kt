package com.example.housify.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Adapter.taskPostedAdapter
import com.example.housify.Models.TaskModel
import com.example.housify.R
import com.example.housify.databinding.FragmentExpensesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class expensesFragment : Fragment(R.layout.fragment_expenses) {
    private lateinit var binding: FragmentExpensesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskArrayList: ArrayList<TaskModel>
    private lateinit var taskAdapter: taskPostedAdapter
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpensesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.taskPostedList
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        taskArrayList = arrayListOf()
        taskAdapter = taskPostedAdapter(taskArrayList)
        recyclerView.adapter = taskAdapter

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
                                        retrieveTasksForRoommate(roommateGroupId)
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

    private fun retrieveTasksForRoommate(roommateGroupId: String) {
        fireStore.collection("taskStorage")
            .whereEqualTo("taskRoommateId", roommateGroupId)
            .get()
            .addOnSuccessListener { taskDocuments ->
                taskArrayList.clear()

                for (taskDocument in taskDocuments) {
                    val taskId = taskDocument.getString("taskId")
                    val taskTitle = taskDocument.getString("taskTitle")
                    val taskDescription = taskDocument.getString("taskDescription")
                    val taskDeadline = taskDocument.getString("taskDeadline")

                    if (taskId != null && taskTitle != null && taskDescription != null && taskDeadline != null) {
                        val taskModel = TaskModel(
                            taskId,
                            taskTitle,
                            taskDescription,
                            taskDeadline,
                            roommateGroupId
                        )
                        taskArrayList.add(taskModel)
                    }
                }

                taskAdapter.notifyDataSetChanged()

                Toast.makeText(
                    requireContext(),
                    "Tasks for roommate group $roommateGroupId retrieved successfully",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error getting tasks: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}