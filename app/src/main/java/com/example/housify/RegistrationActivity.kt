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
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {
    private lateinit var email:TextInputEditText
    private lateinit var password:TextInputEditText
    private lateinit var firstName:TextInputEditText
    private lateinit var lastName:TextInputEditText
    private lateinit var phoneNumber:TextInputEditText
    private lateinit var registerButton:Button
    private lateinit var auth : FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var headToLogin:Button
    private lateinit var fireStore : FirebaseFirestore
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this,PhoneAuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        email = findViewById(R.id.emailRegister)
        password = findViewById(R.id.passwordRegister)
        firstName = findViewById(R.id.userFirstNameRegister)
        lastName = findViewById(R.id.userLastNameRegister)
        phoneNumber = findViewById(R.id.userNumberRegister)
        registerButton = findViewById(R.id.registerButton)
        progressBar = findViewById(R.id.progressRegisterBar)
        headToLogin = findViewById(R.id.headToLoginActivity)
        registerButton.setOnClickListener{
            progressBar.visibility = ProgressBar.VISIBLE
            var enteredEmail = email.text.toString().trim()
            var enteredPassword = password.text.toString().trim()

            var enteredFirstName = firstName.text.toString().trim()
            var enteredLastName = lastName.text.toString().trim()
            var enteredNumber = phoneNumber.text.toString().trim()

            if (enteredEmail.isEmpty()) {
                email.error = "Email is required"
                return@setOnClickListener
            }
            if (enteredPassword.isEmpty()) {
                password.error = "Password is required"
                return@setOnClickListener
            }
            if (enteredFirstName.isEmpty()) {
                password.error = "First Name is required"
                return@setOnClickListener
            }
            if (enteredLastName.isEmpty()) {
                password.error = "Last Name is required"
                return@setOnClickListener
            }
            if (enteredNumber.isEmpty()) {
                password.error = "Phone Number is required"
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
                        val user = UserModel(enteredFirstName,enteredLastName,enteredNumber)
                        val uid = auth.uid
                        if(uid != null){
                            fireStore.collection("User").document(uid).set(user)
                        }
                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(this,"Please verify your email!", Toast.LENGTH_LONG).show()

                        }
                        ?.addOnFailureListener{
                                Toast.makeText(this,"Something wrong happened, try again!", Toast.LENGTH_LONG).show()

                            }

                        val intent = Intent(this,PhoneAuthActivity::class.java)
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
        headToLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)



        }

    }
}