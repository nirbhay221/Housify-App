package com.example.housify.Adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Models.chatModel
import com.example.housify.R
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import java.util.Locale

class userChatAdapter(private val chatList: List<chatModel>) : RecyclerView.Adapter<userChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item_user, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatModel = chatList[position]

        holder.textMessageForUser.text = chatModel.message
        holder.textMessageTimestamp.text = chatModel.timestamp?.let { formatTimestamp(it) }
        val backgroundColor = if (chatModel.senderUid == Firebase.auth.currentUser?.uid) {

            ContextCompat.getColor(holder.itemView.context, R.color.white)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.home_20)
        }
        holder.itemView.setBackgroundColor(backgroundColor)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessageForUser: TextView = itemView.findViewById(R.id.textMessageForUser)
        val textMessageTimestamp: TextView = itemView.findViewById(R.id.textMessageTimestamp)
    }
    private fun formatTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(date)
    }
}