package com.example.housify.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.UserModel
import com.example.housify.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class userSearchedListAdapter (private val userList: ArrayList<UserModel>): RecyclerView.Adapter<userSearchedListAdapter.MyViewHolder>() {
    private  val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var currentUserUid = auth.currentUser?.uid
    private var selectedUsers: MutableList<UserModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userSearchedListAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item_card,parent,false)
        return MyViewHolder(itemView)
    }
    fun getSelectedUsers(): List<UserModel>{
        return selectedUsers.toList()
    }


    override fun onBindViewHolder(holder: userSearchedListAdapter.MyViewHolder, position: Int) {
        val userDetails: UserModel = userList[position]
        holder.userName.text= userDetails.firstName+ " " +userDetails.lastName
        holder.userPhone.text = userDetails.number
        holder.addButton.setOnClickListener{
            if(selectedUsers.contains(userDetails)){
                selectedUsers.remove(userDetails)
            }
            else{selectedUsers.add(userDetails)}

        }
        holder.removeButton.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName : TextView = itemView.findViewById(R.id.userSearchName)
        val userPhone : TextView = itemView.findViewById(R.id.userSearchPhone)
        val addButton : ImageButton = itemView.findViewById(R.id.addButtonUserToRoommate)
        val removeButton : ImageButton = itemView.findViewById(R.id.removeButtonUserToRoommate)

    }
}