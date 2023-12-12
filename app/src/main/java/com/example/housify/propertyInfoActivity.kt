package com.example.housify

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tomtom.sdk.search.Search
import com.tomtom.sdk.search.SearchCallback
import com.tomtom.sdk.search.SearchOptions
import com.tomtom.sdk.search.SearchResponse
import com.tomtom.sdk.search.common.error.SearchFailure
import com.tomtom.sdk.search.online.OnlineSearch

class propertyInfoActivity : AppCompatActivity() {

    private lateinit var propertyLocation: TextView
    private lateinit var propertyTitle: TextView
    private lateinit var propertyBedrooms: TextView
    private lateinit var propertyBaths: TextView
    private lateinit var propertyType: TextView
    private lateinit var propertyYear: TextView
    private lateinit var propertyImages: ImageView
    private lateinit var propertyUid: TextView
    private lateinit var addUserToCurrentUserCollection: Button
    private lateinit var propUid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_info)
        propertyTitle = findViewById(R.id.propertyTitle)
        propertyLocation = findViewById(R.id.propertyLocation)
        propertyBedrooms = findViewById(R.id.propertyBedrooms)
        propertyBaths = findViewById(R.id.propertyBaths)
        propertyType = findViewById(R.id.propertyType)
        propertyYear = findViewById(R.id.propertyYear)
        propertyImages = findViewById<ImageView>(R.id.propertyImages)
        propertyUid = findViewById(R.id.propertyUidInfo)
        addUserToCurrentUserCollection = findViewById(R.id.chatWithUser)

        val searchApi = OnlineSearch.create(this, "eXRlAZJos3TBi0kr7fSrXrp8Kl7Nt1e8")


        val extras = intent.extras
        if (extras != null) {
            val propName = extras.getString("propertyName")
            val propLocation = extras.getString("propertyLocation")
            propUid = extras.getString("propertyUid").toString()
            propertyTitle.text = propName
            propertyBedrooms.text = "3"
            propertyLocation.text = propLocation
            propertyUid.text = propUid
            Toast.makeText(this, propUid,Toast.LENGTH_LONG).show()

            val propertyLocation = propLocation
            val options = propertyLocation?.let { SearchOptions(query = it, limit = 1) }

            if (options != null) {
                val search = searchApi.search(options, object : SearchCallback {

                    override fun onSuccess(result: SearchResponse) {
                        if (result.results.isNotEmpty()) {
                            val firstResult = result.results.first()
                            val place = firstResult.place

                            if (place != null && place.coordinate != null) {
                                val latitude = place.coordinate.latitude
                                val longitude = place.coordinate.longitude


                                Log.d("TomTom", "Latitude: $latitude, Longitude: $longitude")
                                }

//                            val location = firstResult.position
//                            val latitude = location.latitude
//                            val longitude = location.longitude

//                            Log.d("TomTom", "Latitude: $latitude, Longitude: $longitude")
                        } else {

                        }
                    }

                    override fun onFailure(failure: SearchFailure) {
                    }
                })
            }

            propUid?.let { propertyUid ->
                FirebaseFirestore.getInstance().collection("Properties").document(propUid).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            var userImageBase64 = document.getString("userPropertyImages")

                            if (!userImageBase64.isNullOrBlank()) {
                                var decodedImage = decodeBase64ToBitmap(userImageBase64)
                                propertyImages.setImageBitmap(decodedImage)
                            }
                        } else {
                            Log.d("Property View Info", "Property details not found")
                        }

                    }.addOnFailureListener { exception ->
                        Log.e("Property View Info", "Error getting property details", exception)
                    }

                if (propUid != null) {
                    getUserNameFromUid(propUid)
                }
            }
            addUserToCurrentUserCollection.setOnClickListener {
                var fireStore = FirebaseFirestore.getInstance()
                var currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                var selectedUserUid = propUid
                if (currentUserUid != null && selectedUserUid != null) {
                    addToChatCluster(currentUserUid, selectedUserUid)
                    createChatDocument(currentUserUid, selectedUserUid)
                }


            }
        }
    }

        private fun createChatDocument(currentUserUid: String, selectedUserUid: String) {
            var chatCollection = FirebaseFirestore.getInstance().collection("ChatsCollection")
            var chatData = hashMapOf(
                "UserParticipants" to arrayListOf(currentUserUid, selectedUserUid),
                "messageUserUid" to getUniqueMessageUserUid(currentUserUid, selectedUserUid),
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

            chatCollection.whereArrayContains("UserParticipants", currentUserUid)
                .whereArrayContains("UserParticipants", selectedUserUid)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        chatCollection.add(chatData)
                            .addOnSuccessListener { document ->
                                Log.d("propertyInfoActivity", "Chat Document Created")
                                val messageUserUid = chatData["messageUserUid"].toString()
                                chatCollection.document(document.id)
                                    .update(
                                        "UserParticipants",
                                        FieldValue.arrayUnion(selectedUserUid, currentUserUid)
                                    )
                                    .addOnSuccessListener {
                                        Log.d(
                                            "propertyInfoActivity",
                                            "User added to chat successfully"
                                        )
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("propertyInfoActivity", "User add failure", exception)
                                    }
                            }
                    } else {
                        Log.d("propertyInfoActivity", "Chat Document Already Exists")

                    }
                }.addOnFailureListener { exception ->
                    Log.e("propertyInfoActivity", "Error checking chat document")
                }
        }

        private fun getUniqueMessageUserUid(
            currentUserUid: String,
            selectedUserUid: String
        ): String {
            return "${currentUserUid}_${selectedUserUid}_${System.currentTimeMillis()}"
        }

        private fun getUserNameFromUid(uid: String) {
            val firestore = FirebaseFirestore.getInstance()
            val usersCollection = firestore.collection("User")
            if (uid != null) {
                usersCollection.document(uid).get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userFirstName = documentSnapshot.getString("firstName")
                        val userLastName = documentSnapshot.getString("lastName")
//                    propertyUid.text = userFirstName + " "+ userLastName
                        propertyUid.text = userFirstName
                    } else {

                    }
                }.addOnFailureListener { exception ->
                    Log.e("propertyInfoActivity", "Error getting User name", exception)
                }
            }
        }

    private fun addToChatCluster(currentUserUid: String, selectedUserUid: String) {
        var chatUserClusterCollection =
            FirebaseFirestore.getInstance().collection("ChatUserCluster")
        chatUserClusterCollection.document(currentUserUid)
            .update("connectedUsers", FieldValue.arrayUnion(selectedUserUid))
            .addOnSuccessListener {
                Log.d(
                    "propertyInfoActivity",
                    "User $selectedUserUid added to the following $currentUserUid cluster successfully."
                )
                chatUserClusterCollection.document(selectedUserUid)
                    .update("connectedUsers", FieldValue.arrayUnion(currentUserUid))
                    .addOnSuccessListener {
                        Log.d(
                            "propertyInfoActivity",
                            "User $currentUserUid added to the following $selectedUserUid cluster successfully."
                        )
                    }.addOnFailureListener { exception ->
                        Log.e(
                            "propertyInfoActivity",
                            "Errorr in adding user to the chat cluster",
                            exception
                        )
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("propertyInfoActivity", "Error in adding user to the chat cluster", exception)
            }
    }
    private fun decodeBase64ToBitmap(base64:String): Bitmap {
        var decodeByteArray = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodeByteArray,0,decodeByteArray.size)
    }
}





