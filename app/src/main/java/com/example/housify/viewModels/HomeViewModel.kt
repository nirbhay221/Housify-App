package com.example.housify.viewModels

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.housify.HomeActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

const val CURRENT_UPDATEDFIRSTNAME_KEY = "CURRENT_UPDATEDFIRSTNAME_KEY"
const val CURRENT_UPDATEDLASTNAME_KEY = "CURRENT_UPDATEDLASTNAME_KEY"
const val CURRENT_UPDATEDPHONENUMBER_KEY = "CURRENT_UPDATEDPHONENUMBER_KEY"
const val CURRENT_UPDATEDEMAIL_KEY = "CURRENT_UPDATEDEMAIL_KEY"


class HomeViewModel (private val savedStateHandle: SavedStateHandle) : ViewModel(){
    var firstNameSaved: String
        get() = savedStateHandle.get(CURRENT_UPDATEDFIRSTNAME_KEY) ?: ""
        set(value) = savedStateHandle.set(CURRENT_UPDATEDFIRSTNAME_KEY, value)
    var lastNameSaved: String
        get() = savedStateHandle.get(CURRENT_UPDATEDLASTNAME_KEY) ?: ""
        set(value) = savedStateHandle.set(CURRENT_UPDATEDLASTNAME_KEY, value)
    var phoneNumberSaved: String
        get() = savedStateHandle.get(CURRENT_UPDATEDPHONENUMBER_KEY) ?: ""
        set(value) = savedStateHandle.set(CURRENT_UPDATEDPHONENUMBER_KEY, value)
    var userEmailSaved: String
        get() = savedStateHandle.get(CURRENT_UPDATEDEMAIL_KEY) ?: ""
        set(value) = savedStateHandle.set(CURRENT_UPDATEDEMAIL_KEY, value)
    var modifiedFirstName: String
        get() = savedStateHandle.get("MODIFIED_FIRST_NAME") ?: ""
        set(value) = savedStateHandle.set("MODIFIED_FIRST_NAME", value)

    var modifiedLastName: String
        get() = savedStateHandle.get("MODIFIED_LAST_NAME") ?: ""
        set(value) = savedStateHandle.set("MODIFIED_LAST_NAME", value)

    var modifiedPhoneNumber: String
        get() = savedStateHandle.get("MODIFIED_PHONE_NUMBER") ?: ""
        set(value) = savedStateHandle.set("MODIFIED_PHONE_NUMBER", value)

    var modifiedUserEmail: String
        get() = savedStateHandle.get("MODIFIED_USER_EMAIL") ?: ""
        set(value) = savedStateHandle.set("MODIFIED_USER_EMAIL", value)


    val userDataFetched = MutableLiveData<Boolean>()
    var isDataFetched = false

    fun getUserData(user: FirebaseUser, firebaseStore: FirebaseFirestore) {
        if (!isDataFetched) {
            val uid = user.uid
            val userEmail = user.email
            val userRef = firebaseStore.collection("User").document(uid)
            Log.d(TAG,"uid :$uid, userEmail: $userEmail")
            userRef.get().addOnSuccessListener { e ->
                if (e.exists()) {
                    firstNameSaved = e.getString("firstName").toString()
                    lastNameSaved = e.getString("lastName").toString()
                    phoneNumberSaved = e.getString("number").toString()
                    if (userEmail != null) {
                        userEmailSaved = userEmail
                    }
                    userDataFetched.postValue(true)
                    isDataFetched = true
                }
            }
        }
    }
    fun updateUserInfo(firstNameUser:String,lastNameUser:String,phoneNumberUser:String,emailUser:String){
        firstNameSaved = firstNameUser
        lastNameSaved = lastNameUser
        phoneNumberSaved = phoneNumberUser
        userEmailSaved = emailUser
    }

}