package com.example.housify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_user)
        messageSent = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.sendMessages)
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
    fun FirebaseFirestore.generateUniqueId(): String {
        return this.collection("dummyCollection").document().id
    }
}
