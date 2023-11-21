package com.example.housify

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity() {
    private lateinit var phoneNumber:TextInputEditText
    private lateinit var otpText : TextInputEditText
    private lateinit var otpSend: Button
    private lateinit var submitOtp :Button
    private lateinit var resendOtp:Button
    private var verificationCode : String=""
    private lateinit var user:FirebaseUser
    private lateinit var auth:FirebaseAuth

    private var verificationId : String =""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)
        verificationCode =""
        var timeoutSecond:Long = 60L
        var verificationCode:String
        var forceResendingToken:PhoneAuthProvider.ForceResendingToken
        phoneNumber = findViewById(R.id.phoneNumberAuth)
        otpText = findViewById(R.id.otpAuth)
        otpSend = findViewById(R.id.sendOTP)
        submitOtp = findViewById(R.id.enterOTP)
        resendOtp = findViewById(R.id.resendOTP)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        otpSend.setOnClickListener(){

            var builder = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phoneNumber.text.toString())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                            Log.d(TAG,"Verification Passed")
                            Toast.makeText(this@PhoneAuthActivity,"Verification Completed",Toast.LENGTH_LONG).show()

                        }

                        override fun onVerificationFailed(p0: FirebaseException) {

                            Log.d(TAG,"Verification Failed")
                        }

                        override fun onCodeSent(
                            p0: String,
                            p1: PhoneAuthProvider.ForceResendingToken
                        ) {
                            super.onCodeSent(p0, p1)
                            verificationId = p0
                            forceResendingToken = p1
                            Toast.makeText(this@PhoneAuthActivity,"$verificationId",Toast.LENGTH_LONG).show()

                            Toast.makeText(this@PhoneAuthActivity,"OTP sent",Toast.LENGTH_LONG).show()

                        }
                    }
                ).build()
            PhoneAuthProvider.verifyPhoneNumber(builder)
        }
        submitOtp.setOnClickListener {
            var otpSubmitted = otpText.text.toString().trim()
            if(otpSubmitted.isNotEmpty()){
                var credential = PhoneAuthProvider.getCredential(verificationId,otpSubmitted)
                user.linkWithCredential(credential).addOnCompleteListener(this){
                    task ->
                    if(task.isSuccessful){
                        goToHomePage()
                    }
                    else{
                        val exception = task.exception
                        Log.e(TAG, "Authentication Failed: ${exception?.message}")

                        Toast.makeText(this,"Authentication Failed",Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

        }

    private fun goToHomePage() {
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}
