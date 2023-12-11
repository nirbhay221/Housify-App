package com.example.housify

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.housify.Models.TaskModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class assignUserToTask : AppCompatActivity() {
    private lateinit var userUidText: EditText
    private lateinit var taskTimeByUser: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_user_to_task)
        userUidText = findViewById<EditText>(R.id.userIdForTask)
        taskTimeByUser = findViewById<EditText>(R.id.taskTimeAssignedForUser)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        submitButton = findViewById(R.id.updateTaskButton)

        val taskName = intent.getStringExtra("taskName")
        val taskDeadline = intent.getStringExtra("taskDeadline")
        val taskId = intent.getStringExtra("taskId")
        val taskDescription = intent.getStringExtra("taskDescription")

        val toastMessage = "Task ID: $taskId\n"
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

        var currentUserUid = auth.currentUser?.uid
        userUidText.setText(currentUserUid)
        submitButton.setOnClickListener {
            if (currentUserUid != null) {
                firestore.collection("Roommate")
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(this, "No roommate groups found", Toast.LENGTH_LONG)
                                .show()
                            return@addOnSuccessListener
                        }

                        for (document in documents) {
                            val roommatesArray =
                                document.get("roommates") as? ArrayList<Map<String, Any>>

                            if (roommatesArray != null) {
                                for (roommate in roommatesArray) {
                                    val uid = roommate["uid"] as? String
                                    if (uid == currentUserUid) {
                                        val roommateGroupId = document.getString("roommateGroupId")
                                        val roommateGroupName =
                                            document.getString("roommateGroupName")
                                        if (roommateGroupId != null) {
                                            var task = TaskModel(
                                                taskId,
                                                taskName,
                                                taskDescription,
                                                taskDeadline,
                                                roommateGroupId
                                            )
                                            Toast.makeText(
                                                this,
                                                "Title: $taskName, Description: $taskDescription, Deadline: $taskDeadline",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            firestore.collection("taskStorage")
                                                .document(roommateGroupId)
                                                .get()
                                                .addOnSuccessListener { taskStorageDocument ->
                                                    val assignedUsersExist =
                                                        taskStorageDocument.contains("assignedUsers")

                                                    if (!assignedUsersExist) {
                                                        val initialAssignedUsers =
                                                            mutableListOf<Pair<UserModel, String>>()
                                                        firestore.collection("taskStorage")
                                                            .document(roommateGroupId)
                                                            .update(
                                                                "assignedUsers",
                                                                initialAssignedUsers
                                                            )
                                                            .addOnSuccessListener {
                                                                addCurrentUserToAssignedUsers(
                                                                    roommateGroupId,
                                                                    taskId,
                                                                    currentUserUid,
                                                                    taskTimeByUser.text.toString()
                                                                        .trim()
                                                                )
                                                            }
                                                    } else {
                                                        addCurrentUserToAssignedUsers(
                                                            roommateGroupId,
                                                            taskId,
                                                            currentUserUid,
                                                            taskTimeByUser.text.toString().trim()
                                                        )
                                                    }
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

    private fun addCurrentUserToAssignedUsers(
        roommateGroupId: String,
        taskId: String?,
        currentUserUid: String,
        currentTime: String
    ) {
        val assignedUser = hashMapOf(
            "userId" to currentUserUid,
            "timeAssigned" to currentTime
        )

        firestore.collection("taskStorage")
            .document(roommateGroupId)
            .get()
            .addOnSuccessListener { taskStorageDocument ->
                val assignedUsersList =
                    taskStorageDocument.get("assignedUsers") as? ArrayList<HashMap<String, String>>

                if (assignedUsersList != null) {
                    val existingIndex =
                        assignedUsersList.indexOfFirst { it["userId"] == currentUserUid }

                    if (existingIndex != -1) {
                        assignedUsersList[existingIndex]["timeAssigned"] = currentTime
                    } else {
                        assignedUsersList.add(assignedUser)
                    }

                    firestore.collection("taskStorage")
                        .document(roommateGroupId)
                        .update(
                            "assignedUsers",
                            assignedUsersList
                        )
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Task updated successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error updating task: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error getting taskStorage document: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}