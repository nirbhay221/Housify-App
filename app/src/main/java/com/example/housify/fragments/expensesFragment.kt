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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        taskArrayList = arrayListOf()
        taskAdapter = taskPostedAdapter(taskArrayList)
        recyclerView.adapter = taskAdapter

        EventChangeListener()
    }
    private fun EventChangeListener() {
        val currentUserUid = auth.currentUser?.uid
        Toast.makeText(requireContext(), "$currentUserUid", Toast.LENGTH_LONG).show()
        if (currentUserUid != null) {
            fireStore.collection("Roommate")
                .whereArrayContains("roommates", mapOf("uid" to currentUserUid))
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(requireContext(), "No roommate groups found", Toast.LENGTH_LONG).show()
                        return@addOnSuccessListener
                    }

                    for (document in documents) {
                        val roommateGroupId = document.getString("roommateGroupId")
                        if (roommateGroupId != null) {
                            fireStore.collection("Roommate").document(roommateGroupId)
                                .collection("tasks")
                                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                                    override fun onEvent(
                                        value: QuerySnapshot?,
                                        error: FirebaseFirestoreException?
                                    ) {
                                        if (error != null) {
                                            Log.e("Firestore Error", error.message.toString())
                                            return
                                        }
                                        for (document in value!!.documents) {
                                            val taskId = document.getString("taskId")
                                            val taskTitle = document.getString("taskTitle")
                                            val taskDescription = document.getString("taskDescription")
                                            val taskDeadline = document.getString("taskDeadline")
                                            val assignedToCurrentUser = document.getBoolean("assignedToCurrentUser") ?: false

                                            val task = TaskModel(taskId, taskTitle, taskDescription, taskDeadline)
                                            task.assignedToCurrentUser = assignedToCurrentUser

                                            taskArrayList.add(task)
                                        }

                                        taskAdapter.notifyDataSetChanged()
                                    }
                                })
                        }
                    }
                }
        }
    }

}
