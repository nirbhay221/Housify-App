package com.example.housify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var logoutButton : Button
    private lateinit var user:FirebaseUser
    private lateinit var firebaseStore : FirebaseFirestore
    private lateinit var firstNameInfo: TextInputEditText
    private lateinit var lastNameInfo: TextInputEditText
    private lateinit var phoneNumberInfo: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()
        firstNameInfo = findViewById(R.id.firstNameInfo)
        lastNameInfo = findViewById(R.id.lastNameInfo)
        phoneNumberInfo = findViewById(R.id.phoneNumberInfo)
        logoutButton = findViewById(R.id.logoutBtn)
        firebaseStore = FirebaseFirestore.getInstance()
        user = auth.currentUser!!
        if(user == null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val uid = user.uid

        Toast.makeText(this,"$uid",Toast.LENGTH_LONG).show()
        val userRef = firebaseStore.collection("User").document(uid)
        userRef.get().addOnSuccessListener { e ->
            if (e.exists()) {
                var firstName = e.getString("firstName")
                var lastName = e.getString("lastName")
                var phoneNumber = e.getString("number")
                firstNameInfo.setText(firstName)
                lastNameInfo.setText(lastName)
                phoneNumberInfo.setText(phoneNumber)
            }

        }

        logoutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}