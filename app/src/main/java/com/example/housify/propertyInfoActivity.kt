package com.example.housify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class propertyInfoActivity : AppCompatActivity() {

    private lateinit var propertyLocation:TextView
    private lateinit var propertyName:TextView
    private lateinit var propertyUid:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_info)
        propertyLocation = findViewById(R.id.propertyLocationInfo)
        propertyName = findViewById(R.id.propertyNameInfo)
        propertyUid = findViewById(R.id.propertyUidInfo)
        val extras = intent.extras
        if (extras != null) {
            val propName = extras.getString("propertyName")
            val propLocation = extras.getString("propertyLocation")
            val propUid = extras.getString("propertyUid")
            propertyName.text = propName
            propertyLocation.text = propLocation
            propertyUid.text = propUid
            if (propUid != null) {
                getUserNameFromUid(propUid)
            }
        }
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
                    propertyUid.text = userFirstName + " "+ userLastName
                }else{

                }
            }.addOnFailureListener{exception ->
                Log.e("propertyInfoActivity","Error getting User name",exception)
            }
        }
    }
}