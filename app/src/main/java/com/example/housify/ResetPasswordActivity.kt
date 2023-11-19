package com.example.housify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var emailReset:TextInputEditText
    private lateinit var resetBtn:Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        emailReset=findViewById(R.id.emailReset)
        resetBtn = findViewById(R.id.resetButton)
        auth = FirebaseAuth.getInstance()
        resetBtn.setOnClickListener{
            var emailNotify = emailReset.text.toString()
            auth.sendPasswordResetEmail(emailNotify).addOnSuccessListener {
                Toast.makeText(this,"Please check your Mail.",Toast.LENGTH_LONG).show()

            }.addOnFailureListener{
                Toast.makeText(this,"Something went Wrong !",Toast.LENGTH_LONG).show()
            }


        }
    }
}