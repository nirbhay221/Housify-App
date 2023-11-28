package com.example.housify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var password:TextInputEditText

    private lateinit var loginButton:Button
    private lateinit var auth : FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var headToRegister:Button
    private lateinit var resetPassword:TextView
    private lateinit var client : GoogleSignInClient
    private lateinit var signInWithGoogleButton:Button
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this,NavigationActivity::class.java)
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
        resetPassword = findViewById(R.id.resetPassword)
        signInWithGoogleButton = findViewById(R.id.signInWithGoogleBtn)
        auth = FirebaseAuth.getInstance()

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("723749140504-f8phj118gis4iid89fibc4715i8sriq5.apps.googleusercontent.com")
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this,gso)


        headToRegister.setOnClickListener{
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
        resetPassword.setOnClickListener{
            val intent = Intent(this,ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
        signInWithGoogleButton.setOnClickListener{
            val intent = client.signInIntent
            startActivityForResult(intent,1234)
        }

        loginButton.setOnClickListener{
            progressBar.visibility = ProgressBar.VISIBLE
            val enteredEmail = email.text.toString().trim()
            val enteredPassword = password.text.toString().trim()
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
                        var verified = auth.currentUser?.isEmailVerified
                        if(verified == true){
                            val user = auth.currentUser
                            val intent = Intent(this,NavigationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Please verify your email first",Toast.LENGTH_LONG).show()

                        }
                        Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
//
//                        val intent = Intent(this,HomeActivity::class.java)
//                        startActivity(intent)
//                        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1234){
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                var account:GoogleSignInAccount = task.getResult(ApiException::class.java)
                var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                    signInTask ->
                    if(signInTask.isSuccessful){
                        val googleIntent = Intent(this,NavigationActivity::class.java)
                        startActivity(googleIntent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Google Sign In Failed: ${signInTask.exception?.message}",Toast.LENGTH_LONG).show()


                    }
                }
            }catch(e:ApiException){
                e.printStackTrace()
                Toast.makeText(this,"Google SignIn Failed: ${e.statusCode}",Toast.LENGTH_LONG).show()
            }
        }
    }



}