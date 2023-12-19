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
import com.example.housify.Models.eventModel
import com.example.housify.R
import com.example.housify.assignUserToTask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class eventListViewAdapter (private val eventList: ArrayList<eventModel>): RecyclerView.Adapter<eventListViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): eventListViewAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item_list_card,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: eventListViewAdapter.MyViewHolder, position: Int) {
        val event: eventModel = eventList[position]
        holder.eventTitle.text = event.eventTitle
        holder.eventTotalTransactionAmount.text = event.eventTransaction + " $"
        val currentUserUid = FirebaseAuth.getInstance().uid
        val eventTitle = event.eventTitle

        if (eventTitle != null) {
            val firestore = FirebaseFirestore.getInstance()
            val eventsCollection = firestore.collection("eventStorage")
            eventsCollection.whereEqualTo("eventTitle", eventTitle)
                .get()
                .addOnSuccessListener { eventDocuments ->
                    for (eventDocument in eventDocuments) {
                        val assignedUsers = eventDocument.get("assignedUsers") as? ArrayList<Map<String, Any>>
                        assignedUsers?.let {
                            for (userMap in it) {
                                val firstMap = userMap["first"] as? Map<String, Any>
                                val uid = firstMap?.get("uid") as? String

                                if (uid == currentUserUid) {
                                    Log.d("Assigned User", "User found in assignedUsers")

                                    Log.d("Assigned User Money", "${userMap}")
                                    val amountPaid = userMap["second"] as? String

                                    holder.eventTotalAmountPaid.text = "$amountPaid $"
                                    return@addOnSuccessListener
                                }
                            }
                        }
                    }
                    holder.eventTotalAmountPaid.text = "You are not assigned to this event."
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore Query", "Error getting documents: ${e.message}")
                       }
        }
        var currentUserAssignment = event.assignedUsers.size
        Log.d("Assigned Users Array", "$currentUserAssignment")

    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var eventTitle : TextView = itemView.findViewById(R.id.eventNameListed)
        var eventTotalTransactionAmount : TextView = itemView.findViewById(R.id.eventTotalTransactionAmountValue)
        var eventTotalAmountPaid : TextView = itemView.findViewById(R.id.eventTransactionAmountValue)
    }
}