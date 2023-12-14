package com.example.housify.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.R
import com.example.housify.UserModel
import com.example.housify.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class eventUserAdapter(private val userList: ArrayList<UserModel>): RecyclerView.Adapter<eventUserAdapter.MyViewHolder>() {
    private  val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var currentUserUid = auth.currentUser?.uid
    private var selectedUsers: MutableList<UserModel> = mutableListOf()
    private var selectedUsersWithMoney: MutableList<Pair<UserModel,String>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): eventUserAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_user_item_card,parent,false)
        return MyViewHolder(itemView)
    }
    fun getSelectedUsers(): List<UserModel>{
        return selectedUsers.toList()
    }
    fun getSelectedUsersWithMoney(): List<Pair<UserModel,String>>{
        return selectedUsersWithMoney
    }

    override fun onBindViewHolder(holder: eventUserAdapter.MyViewHolder, position: Int) {
        val userDetails: UserModel = userList[position]
        holder.userName.text= userDetails.firstName
        holder.userPhone.text = userDetails.number

        var moneyForUser = holder.moneyValue.text.toString()
        holder.addButton.setOnClickListener{

            if(selectedUsers.contains(userDetails)){
                selectedUsers.remove(userDetails)
            }
            else{selectedUsers.add(userDetails)}
            if(selectedUsersWithMoney.contains(Pair(userDetails,moneyForUser))){
                selectedUsersWithMoney.remove(Pair(userDetails,moneyForUser))
            }
            else{

                selectedUsersWithMoney.add(Pair(userDetails,moneyForUser))
                Log.d("SELECTED USER WITH MONEY : ","$selectedUsers")
                for ((user, money) in selectedUsersWithMoney) {
                    Log.d("USER MONEY : ", "${userDetails.uid} ${user.lastName} - $money")
                }
            }

        }
        holder.removeButton.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName : TextView = itemView.findViewById(R.id.eventUserSearchName)
        val userPhone : TextView = itemView.findViewById(R.id.eventUserSearchPhone)
        val addButton : ImageButton = itemView.findViewById(R.id.addButtonUserToEvent)
        val removeButton : ImageButton = itemView.findViewById(R.id.removeButtonUserToEvent)
        val moneyValue : EditText = itemView.findViewById(R.id.eventMoneyPaidByUserTextView)
    }
}