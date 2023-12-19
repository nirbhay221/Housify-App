package com.example.housify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Adapter.userChatAdapter
import com.example.housify.Models.chatModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class chatUserActivity : AppCompatActivity() {
    private var auth:FirebaseAuth = FirebaseAuth.getInstance()
    private var userUid : String = ""
    private lateinit var messageSent:TextView
    private lateinit var firestore : FirebaseFirestore
    private lateinit var sendButton: Button
    private lateinit var recyclerViewChat: RecyclerView

    private lateinit var chatUserAdapter: userChatAdapter

    private val chatList = mutableListOf<chatModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_user)
        messageSent = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.sendMessages)

        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        var currentUserUid = auth?.currentUser?.uid

        val extras = intent.extras
        if (extras != null) {
            val receivedFirstName = extras.getString("sentFirstName")
            val receivedLastName = extras.getString("sentLastName")
            val receivedNumber = extras.getString("sentNumber")
            val receivedUserImage = extras.getString("sentUserImage")
            val receivedUserUid = extras.getString("sentUserUid")
            userUid = receivedUserUid.toString()
    }
        setupRecyclerView()
        fetchChatMessages()

        sendButton.setOnClickListener{
            val chat = chatModel(
                senderUid = auth.currentUser?.uid,
                receiverUid = userUid,
                message = messageSent.text.toString(),
                timestamp = Timestamp.now()
            )
            firestore = FirebaseFirestore.getInstance()
            var chatStorageCollection = firestore.collection("ChatStorage")
            var chatMessageUid = firestore.generateUniqueId()


            firestore.collection("userMessageStorage")
                .document(chatMessageUid)
                .set(chat)
                .addOnSuccessListener {
                    chatList.add(chat)

                    chatUserAdapter.notifyItemInserted(chatList.size - 1)

                    Toast.makeText(
                        this,
                        "Task added successfully to chat storage.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Error adding task: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }

//            var currentUserUid = auth?.currentUser?.uid
//            val chat = chatModel(
//                senderUid = auth.currentUser?.uid,
//                receiverUid = userUid,
//                message = messageSent.text.toString(),
//                timestamp = Timestamp.now()
//            )
//            firestore = FirebaseFirestore.getInstance()
//            var chatStorageCollection = firestore.collection("ChatStorage")
//            var chatMessageUid = firestore.generateUniqueId()
//
//
//            firestore.collection("userMessageStorage")
//                .document(chatMessageUid)
//                .set(chat)
//                .addOnSuccessListener {
//                    Toast.makeText(
//                        this,
//                        "Task added successfully to chat storage.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(
//                        this,
//                        "Error adding task: ${e.message}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//        }
//
//

}

    private fun fetchChatMessages() {
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("userMessageStorage")
            .whereEqualTo("senderUid", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { senderMessages ->
                for (document in senderMessages) {
                    val chat = document.toObject(chatModel::class.java)
                    chatList.add(chat)
                }
                chatUserAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching messages: ${e.message}", Toast.LENGTH_LONG).show()
            }

        firestore.collection("userMessageStorage")
            .whereEqualTo("receiverUid", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { receiverMessages ->
                for (document in receiverMessages) {
                    val chat = document.toObject(chatModel::class.java)
                    chatList.add(chat)
                }
                chatUserAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching messages: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


private fun setupRecyclerView() {
        chatUserAdapter = userChatAdapter(chatList)
        recyclerViewChat.adapter = chatUserAdapter
        recyclerViewChat.layoutManager = LinearLayoutManager(this)
    }

    fun FirebaseFirestore.generateUniqueId(): String {
        return this.collection("dummyCollection").document().id
    }
}
