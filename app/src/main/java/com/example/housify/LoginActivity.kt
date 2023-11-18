package com.example.housify

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var password:TextInputEditText

    private lateinit var loginButton:Button
    private lateinit var auth : FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var headToRegister:Button

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
        setContentView(R.layout.activity_login)
        headToRegister = findViewById(R.id.headToRegisterActivity)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressLoginBar)
        auth = FirebaseAuth.getInstance()
        headToRegister.setOnClickListener{
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
        loginButton.setOnClickListener{
            progressBar.visibility = ProgressBar.VISIBLE
            var enteredEmail = email.text.toString().trim()
            var enteredPassword = password.text.toString().trim()
            if(enteredEmail.isEmpty()){
                Toast.makeText(this,"Enter Email!",Toast.LENGTH_LONG).show()

            }
            if(enteredPassword.isEmpty()){
            Toast.makeText(this,"Enter Password!",Toast.LENGTH_LONG).show()
        }
            auth.signInWithEmailAndPassword(enteredEmail,enteredPassword)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = ProgressBar.INVISIBLE
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Looin Successful",Toast.LENGTH_LONG).show()
                        val intent = Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }
}