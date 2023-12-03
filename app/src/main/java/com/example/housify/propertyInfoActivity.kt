package com.example.housify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.housify.Models.messageModel
import com.example.housify.Models.propertyModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class propertyInfoActivity : AppCompatActivity() {

    private lateinit var propertyLocation:TextView
    private lateinit var propertyName:TextView
    private lateinit var propertyUid:TextView
    private lateinit var addUserToCurrentUserCollection:Button
    private lateinit var propUid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_info)
        propertyLocation = findViewById(R.id.propertyLocationInfo)
        propertyName = findViewById(R.id.propertyNameInfo)
        propertyUid = findViewById(R.id.propertyUidInfo)
        addUserToCurrentUserCollection = findViewById(R.id.chatWithUser)

        val extras = intent.extras
        if (extras != null) {
            val propName = extras.getString("propertyName")
            val propLocation = extras.getString("propertyLocation")
            propUid = extras.getString("propertyUid").toString()
            propertyName.text = propName
            propertyLocation.text = propLocation
            propertyUid.text = propUid
            if (propUid != null) {
                getUserNameFromUid(propUid)
            }
        }
        addUserToCurrentUserCollection.setOnClickListener{
            var fireStore = FirebaseFirestore.getInstance()
            var currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
            var selectedUserUid = propUid
            if(currentUserUid != null && selectedUserUid != null){
                addToChatCluster(currentUserUid,selectedUserUid)
                createChatDocument(currentUserUid,selectedUserUid)
        }


}
    }

    private fun createChatDocument(currentUserUid: String, selectedUserUid: String) {
        var chatCollection = FirebaseFirestore.getInstance().collection("ChatsCollection")
        var chatData = hashMapOf(
            "UserParticipants" to arrayListOf(currentUserUid,selectedUserUid),
            "messageUserUid" to getUniqueMessageUserUid(currentUserUid,selectedUserUid),
            "timestamp" to FieldValue.serverTimestamp()
        )
//        chatCollection.add(chatData).addOnSuccessListener {
//            document-> Log.d("propertyInfoActivity","Chat Document created")
//            chatCollection.document(document.id).update("UserParticipants",FieldValue.arrayUnion(selectedUserUid))
//                .addOnSuccessListener { Log.d("propertyInfoActivity","User added to chat successfully")
//                chatCollection.document(document.id).update("UserParticipants",FieldValue.arrayUnion(currentUserUid)).
//                addOnSuccessListener { Log.d("propertInfoActivity","User added to chat") }
//                    .addOnFailureListener{
//                        exception ->
//                    Log.e("propertyInfoActivity","User add failure",exception)}}
//                .addOnFailureListener{
//                        exception ->
//                    Log.e("propertyInfoActivity","User add failure",exception)
//                }
//
//        }.addOnFailureListener{exception ->
//            Log.e("propertyInfoActivity","User add failure",exception)}

        chatCollection.whereArrayContains("UserParticipants",currentUserUid)
            .whereArrayContains("UserParticipants",selectedUserUid)
            .get()
            .addOnSuccessListener { documents ->
                if(documents.isEmpty){
                    chatCollection.add(chatData)
                        .addOnSuccessListener { document ->
                            Log.d("propertyInfoActivity","Chat Document Created")
                            val messageUserUid = chatData["messageUserUid"].toString()
                            chatCollection.document(document.id)
                                .update("UserParticipants",FieldValue.arrayUnion(selectedUserUid,currentUserUid))
                                .addOnSuccessListener { Log.d("propertyInfoActivity","User added to chat successfully") }
                                .addOnFailureListener{ exception ->
                                    Log.e("propertyInfoActivity","User add failure",exception)
                                }
                        }
                }
                else{
                    Log.d("propertyInfoActivity","Chat Document Already Exists")

                }
            }.addOnFailureListener{exception ->
                Log.e("propertyInfoActivity","Error checking chat document")
            }
    }

    private fun getUniqueMessageUserUid(currentUserUid: String,selectedUserUid: String): String {
        return "${currentUserUid}_${selectedUserUid}_${System.currentTimeMillis()}"
    }

    private fun getUserNameFromUid(uid:String){
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection("User")
        if(uid != null){
            usersCollection.document(uid).get().addOnSuccessListener {
                documentSnapshot ->
                if(documentSnapshot.exists()){
                    val userFirstName = documentSnapshot.getString("firstName")
                    val userLastName = documentSnapshot.getString("lastName")
//                    propertyUid.text = userFirstName + " "+ userLastName
                    propertyUid.text = userFirstName
                }else{

                }
            }.addOnFailureListener{exception ->
                Log.e("propertyInfoActivity","Error getting User name",exception)
            }
        }
    }
}

    private fun addToChatCluster(currentUserUid: String, selectedUserUid: String) {
        var chatUserClusterCollection = FirebaseFirestore.getInstance().collection("ChatUserCluster")
        chatUserClusterCollection.document(currentUserUid).update("connectedUsers",FieldValue.arrayUnion(selectedUserUid))
            .addOnSuccessListener {
                Log.d("propertyInfoActivity","User $selectedUserUid added to the following $currentUserUid cluster successfully.")
                chatUserClusterCollection.document(selectedUserUid).update("connectedUsers",FieldValue.arrayUnion(currentUserUid))
                    .addOnSuccessListener { Log.d("propertyInfoActivity","User $currentUserUid added to the following $selectedUserUid cluster successfully.")
                    }.addOnFailureListener{
                        exception->
                            Log.e("propertyInfoActivity","Errorr in adding user to the chat cluster",exception)
                    }
            }
            .addOnFailureListener{
                exception ->
                Log.e("propertyInfoActivity","Error in adding user to the chat cluster",exception)
            }
    }




