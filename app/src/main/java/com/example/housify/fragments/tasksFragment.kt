package com.example.housify.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.housify.Models.TaskModel
import com.example.housify.R
import com.example.housify.UserModel
import com.example.housify.databinding.FragmentExpensesBinding
import com.example.housify.databinding.FragmentTasksBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class tasksFragment : Fragment(R.layout.fragment_tasks) {
    private lateinit var binding: FragmentTasksBinding
    private var firestore:FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth  = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var taskTitleText = binding.taskTitleEditText
        var taskDescriptionText = binding.taskDescriptionEditText
        var taskDeadlineText = binding.taskDeadlineEditText
        var submitTaskButton = binding.submitTaskButton

        var currentUserUid = auth.currentUser?.uid
        Toast.makeText(requireContext(),"$currentUserUid",Toast.LENGTH_LONG).show()
        submitTaskButton.setOnClickListener{
            var taskTitle = binding.taskTitleEditText.text.toString().trim()
            var taskDescription = binding.taskDescriptionEditText.text.toString().trim()
            var taskDeadline = binding.taskDeadlineEditText.text.toString().trim()
            var taskId = firestore.generateUniqueId()
            if(currentUserUid != null && taskTitle.isNotEmpty() && taskDescription.isNotEmpty() && taskDeadline.isNotEmpty()){
                firestore.collection("Roommate")
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(requireContext(), "No roommate groups found", Toast.LENGTH_LONG).show()
                            return@addOnSuccessListener
                        }

                        for (document in documents) {
                            val roommatesArray = document.get("roommates") as? ArrayList<Map<String, Any>>

                            if (roommatesArray != null) {
                                for (roommate in roommatesArray) {
                                    val uid = roommate["uid"] as? String
                                    if (uid == currentUserUid) {
                                        val roommateGroupId = document.getString("roommateGroupId")
                                        val roommateGroupName = document.getString("roommateGroupName")
                                        if(roommateGroupId != null){
                                            var task = TaskModel(taskId,taskTitle,taskDescription,taskDeadline)
                                            Toast.makeText(requireContext(), "Title: $taskTitle, Description: $taskDescription, Deadline: $taskDeadline", Toast.LENGTH_LONG).show()

                                            firestore.collection("Roommate").document(roommateGroupId)
                                                .update("tasks", FieldValue.arrayUnion(task))
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Task added successfully to $roommateGroupName",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Error adding task: ${e.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        }
                                        return@addOnSuccessListener

                                        }
                                    }
                                }
                            }
                        }
                        }


            }

        }
    fun FirebaseFirestore.generateUniqueId():String{
        return this.collection("dummyCollection").document().id
    }
//        if(currentUserUid != null){
//            firestore.collection("Roommate")
//                .get()
//                .addOnSuccessListener { documents ->
//                    if (documents.isEmpty) {
//                        Toast.makeText(requireContext(), "No roommate groups found", Toast.LENGTH_LONG).show()
//                        return@addOnSuccessListener
//                    }
//
//                    for (document in documents) {
//                        val roommatesArray = document.get("roommates") as? ArrayList<Map<String, Any>>
//
//                        if (roommatesArray != null) {
//                            for (roommate in roommatesArray) {
//                                val uid = roommate["uid"] as? String
//                                if (uid == currentUserUid) {
//                                    val roommateGroupId = document.getString("roommateGroupId")
//                                    val roommateGroupName = document.getString("roommateGroupName")
//                                    val message = "User is part of Roommate Group: $roommateGroupName ($roommateGroupId)"
//                                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
//                                    return@addOnSuccessListener
//                                }
//                            }
//                        }
//                    }
//                    Toast.makeText(requireContext(), "User is not part of any Roommate Group", Toast.LENGTH_LONG).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(requireContext(), "Failed to retrieve roommate groups", Toast.LENGTH_LONG).show()
//                }
//
//
//        }

    }
