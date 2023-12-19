package com.example.housify.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Adapter.chatListViewAdapter
import com.example.housify.Models.chatModel
import com.example.housify.R
import com.example.housify.databinding.FragmentChatBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class chatFragment : Fragment(R.layout.fragment_chat) {
    private lateinit var binding: FragmentChatBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatArrayList: ArrayList<chatModel>
    private lateinit var chatListAdapter : chatListViewAdapter
    private lateinit var fireStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.chatUserRetrieveList
        recyclerView.layoutManager= LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        chatArrayList= arrayListOf()
        chatListAdapter= chatListViewAdapter(chatArrayList)
        recyclerView.adapter = chatListAdapter
        binding.chatUserRetrieveList


        EventChangeListener()

    }
    private fun EventChangeListener() {
        fireStore = FirebaseFirestore.getInstance()
        val uniqueReceiverUid = HashSet<String>()

        fireStore.collection("userMessageStorage").addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val chatModel = dc.document.toObject(chatModel::class.java)

                        if (uniqueReceiverUid.add(chatModel.receiverUid.toString())) {
                            chatArrayList.add(chatModel)

                            fetchUserDetails(chatModel.receiverUid.toString())

                        }
                    }
                }
                chatListAdapter.notifyDataSetChanged()
            }
        })
    }
    fun fetchUserDetails(userUid: String):String {
        var firstName:String = ""
        var lastName:String = ""
        fireStore.collection("User").document(userUid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    firstName = document.getString("firstName").toString()
                    lastName = document.getString("lastName").toString()

                    Log.d("User Details", "First Name: $firstName, Last Name: $lastName")
                } else {
                    Log.d("User Details", "User document does not exist")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore Query", "Error getting user document: ${e.message}")
            }
        return firstName+" "+lastName
    }


}