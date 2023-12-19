package com.example.housify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.housify.viewModels.HomeViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

// Home page activity, lists properties and search filters
// has fragments for liked property, nearby
class HomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var auth : FirebaseAuth
    private lateinit var logoutButton : Button
    private lateinit var user:FirebaseUser
    private lateinit var firebaseStore : FirebaseFirestore
    private lateinit var firstNameInfo: TextInputEditText
    private lateinit var lastNameInfo: TextInputEditText
    private lateinit var phoneNumberInfo: TextInputEditText
    private lateinit var mailUserInfo : TextInputEditText
    private lateinit var deleteBtn : Button
    private lateinit var updateBtn : Button
    private var isPhoneUpdated:Boolean = false;
    private var isEmailUpdated:Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        firstNameInfo = findViewById(R.id.firstNameInfo)
        lastNameInfo = findViewById(R.id.lastNameInfo)
        phoneNumberInfo = findViewById(R.id.phoneNumberInfo)
        logoutButton = findViewById(R.id.logoutBtn)
        mailUserInfo=findViewById(R.id.mailUserInfo)
        deleteBtn = findViewById(R.id.deleteUserDetail)
        updateBtn = findViewById(R.id.updateUserDetails)
        firstNameInfo.setText(viewModel.firstNameSaved)
        lastNameInfo.setText(viewModel.lastNameSaved)
        phoneNumberInfo.setText(viewModel.phoneNumberSaved)
        mailUserInfo.setText(viewModel.userEmailSaved)
        firebaseStore = FirebaseFirestore.getInstance()
        user = auth.currentUser!!
        if(user == null){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val uid = user.uid
        val userEmail = user.email
        Toast.makeText(this,"$uid",Toast.LENGTH_LONG).show()
        val userRef = firebaseStore.collection("User").document(uid)
        if (savedInstanceState == null) {
            viewModel.getUserData(user, firebaseStore)
        }
        viewModel.userDataFetched.observe(this, Observer { dataFetched ->
            if (dataFetched) {
                firstNameInfo.setText(viewModel.firstNameSaved)
                lastNameInfo.setText(viewModel.lastNameSaved)
                phoneNumberInfo.setText(viewModel.phoneNumberSaved)
                mailUserInfo.setText(viewModel.userEmailSaved)
            }
        })




        updateBtn.setOnClickListener{
            var updatedFirstName = firstNameInfo.text.toString()
            var updatedLastName = lastNameInfo.text.toString()
            var updatedPhoneNumber = phoneNumberInfo.text.toString()
            var updatedEmail = mailUserInfo.text.toString()
            val updatedUserData = hashMapOf(
                "firstName" to updatedFirstName,
                "lastName" to updatedLastName,
                "number" to updatedPhoneNumber
            )
            if(updatedPhoneNumber != user.phoneNumber){
                isPhoneUpdated = true
            }
            if(updatedEmail != user.email){
                isEmailUpdated = true
            }
            userRef.update(updatedUserData as Map<String, Any>).addOnSuccessListener {
                Toast.makeText(this,"User Details updated successfully",Toast.LENGTH_LONG).show()
                if(isPhoneUpdated){
                    val intent = Intent(this,PhoneAuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                isPhoneUpdated = false
            }
                .addOnFailureListener{
                    Toast.makeText(this,"User Details updated failure",Toast.LENGTH_LONG).show()

                }

            if(isEmailUpdated){

                user?.updateEmail(updatedEmail)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Email Updated Successfully", Toast.LENGTH_SHORT)
                            .show()
                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(this, "Please verify your email!", Toast.LENGTH_LONG)
                                .show()

                        }
                            ?.addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Something wrong happened, try again!",
                                    Toast.LENGTH_LONG
                                ).show()
                                isEmailUpdated = false
                            }

                    } else {
                        Toast.makeText(this, "Email Update Failure", Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }


        deleteBtn.setOnClickListener{
            if(user!=null){
                user.delete().addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        firebaseStore.collection("User").document(user.uid)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(this,"User and associated data deleted.",Toast.LENGTH_SHORT).show()
                                var intent = Intent(this,LoginActivity::class.java)
                                startActivity(intent)
                                finish()


                            }.addOnFailureListener{
                                    e->
                                Log.e("DeleteUser", "Failed to delete associated data: ${e.message}", e)
                                Toast.makeText(this,"Failure",Toast.LENGTH_LONG).show()

                            }
                    }else {
                        Toast.makeText(this, "Failed to delete user.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        logoutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}