package com.example.housify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    private lateinit var email:TextInputEditText
    private lateinit var password:TextInputEditText
    private lateinit var registerButton:Button
    private lateinit var auth : FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var headToLogin:Button
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.emailRegister)

        password = findViewById(R.id.passwordRegister)
        registerButton = findViewById(R.id.registerButton)
        progressBar = findViewById(R.id.progressRegisterBar)
        headToLogin = findViewById(R.id.headToLoginActivity)
        registerButton.setOnClickListener{
            progressBar.visibility = ProgressBar.VISIBLE
            var enteredEmail = email.text.toString().trim()
            var enteredPassword = password.text.toString().trim()
            if (enteredEmail.isEmpty()) {
                email.error = "Email is required"
                return@setOnClickListener
            }
            if (enteredPassword.isEmpty()) {
                password.error = "Password is required"
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = ProgressBar.INVISIBLE
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Account created.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

            }
        headToLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)


        }

    }
}