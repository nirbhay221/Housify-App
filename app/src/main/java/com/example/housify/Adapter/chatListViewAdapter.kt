package com.example.housify.Adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Models.TaskModel
import com.example.housify.Models.chatModel
import com.example.housify.R
import com.example.housify.assignUserToTask
import com.example.housify.fragments.chatFragment
import com.google.firebase.firestore.FirebaseFirestore

class chatListViewAdapter(private val chatList: ArrayList<chatModel>): RecyclerView.Adapter<chatListViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_chat_user_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val chat: chatModel = chatList[position]
        var firstName:String = ""
        var lastName:String = ""
        FirebaseFirestore.getInstance().collection("User").document(chat.receiverUid.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    firstName = document.getString("firstName").toString()
                    lastName = document.getString("lastName").toString()

                    holder.chatRecipientName.text = firstName
                    Log.d("User Details", "First Name: $firstName, Last Name: $lastName")
                } else {
                    Log.d("User Details", "User document does not exist")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore Query", "Error getting user document: ${e.message}")

            }


    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chatRecipientName: TextView = itemView.findViewById(R.id.chatUserSearchName)
    }

    private fun getUserNameFromUid(uid: String):String {
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection("User")
        var sentFirstName:String
        var sentLastName:String
        var sentNumber: String
        var sentUserUid: String
        var userPhoneNumber = ""
        if (uid != null) {
            usersCollection.document(uid).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userFirstName = documentSnapshot.getString("firstName")
                    val userLastName = documentSnapshot.getString("lastName")
                    sentFirstName = userFirstName.toString()
                    sentLastName = userLastName.toString()
                    sentNumber = documentSnapshot.getString("number").toString()
                    sentUserUid  = documentSnapshot.getString("uid").toString()
                    userPhoneNumber= documentSnapshot.getString("number").toString()

                }
            }
            return userPhoneNumber
        }
        return userPhoneNumber
    }

}
