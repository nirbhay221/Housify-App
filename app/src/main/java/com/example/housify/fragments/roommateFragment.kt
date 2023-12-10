package com.example.housify.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.housify.Adapter.userSearchedListAdapter
import com.example.housify.Models.roommateModel
import com.example.housify.R
import com.example.housify.UserModel
import com.example.housify.databinding.FragmentExpensesBinding
import com.example.housify.databinding.FragmentRoommatesBinding
import com.example.housify.userModel
import com.google.firebase.firestore.FirebaseFirestore

class roommateFragment: Fragment(R.layout.fragment_roommates) {
    private lateinit var binding: FragmentRoommatesBinding
    private lateinit var userAdapter: userSearchedListAdapter
    private val userList: ArrayList<UserModel> = ArrayList()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoommatesBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = userSearchedListAdapter(userList)
        binding.userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.userRecyclerView.adapter= userAdapter
        binding.searchButton.setOnClickListener{
            findUsers(binding.roommateSearchView.query.toString())
        }

        binding.addRoommateButton.setOnClickListener{
            var roommateGroupName = binding.groupNameEditText.text.toString().trim()
            if(roommateGroupName.isEmpty()) return@setOnClickListener
            var selectedUser = userAdapter.getSelectedUsers()
            if(selectedUser.isEmpty()) return@setOnClickListener
            var roommateGroup = roommateModel()
            roommateGroup.roommateGroupName = roommateGroupName
            roommateGroup.roommates.addAll(selectedUser)
            firestore.collection("Roommate")
                .add(roommateGroup)
                .addOnSuccessListener {
                    documentReference ->
                    var generatedUniqueRoommateGroupId= documentReference.id
                    roommateGroup.roommateGroupId = generatedUniqueRoommateGroupId
                    documentReference.update("roommateGroupId", generatedUniqueRoommateGroupId)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Roommate group added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                "Error updating roommate group ID: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    Toast.makeText(requireContext(), "Roommate group added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error adding roommate group: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }



    }

    private fun findUsers(query: String) {
        userList.clear()
        userAdapter.notifyDataSetChanged()

        if (TextUtils.isEmpty(query)) {

            firestore.collection("User").get().addOnSuccessListener { result ->
                for (document in result) {
                    val user = document.toObject(UserModel::class.java)
                    userList.add(user)
                }
                requireActivity().runOnUiThread {
                    userAdapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
        } else {

            firestore.collection("User").get().addOnSuccessListener { result ->
                for (document in result) {
                    val user = document.toObject(UserModel::class.java)
                    if (TextUtils.isEmpty(query) || user.firstName.equals(query, ignoreCase = true)) {
                        userList.add(user)
                    }
                }

                requireActivity().runOnUiThread {
                    userAdapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }
        }
    }



}
