package com.example.housify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var logoutButton : Button
    private lateinit var user:FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()
        logoutButton = findViewById(R.id.logoutBtn)
        user = auth.currentUser!!
        if(user == null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        logoutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}