package com.example.housify

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.BuildConfig
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File

// user signup and registration activity
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
    private lateinit var userProfileImage:ImageView
    private lateinit var uploadImageButton:ImageButton
    private lateinit var cameraCaptureImageButton:ImageButton
    private lateinit var storage:FirebaseStorage
    private lateinit var currentPhotoPath:String
    private var imageUploaded: ImageView? = null
    private var uri : Uri? =null
    private var CAMERA_PHOTO= 1
    private var FOLDER_PHOTO = 2
    private val CAMERA_CODE_REQ = 10

    private var userImageBase64Converted : String? =null
    private val UPLOAD_IMAGE_CODE_REQ = 20
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
        userProfileImage = findViewById(R.id.userProfileImage)
        uploadImageButton = findViewById(R.id.imageUploadButton)
        cameraCaptureImageButton = findViewById(R.id.imageCaptureButton)
        imageUploaded = findViewById(R.id.userProfileImage)

        cameraCaptureImageButton.setOnClickListener{
//            capturePhoto()
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,CAMERA_CODE_REQ)
        }
        uploadImageButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent,UPLOAD_IMAGE_CODE_REQ)
        }



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

                        val uid = auth.uid
                        val user = userImageBase64Converted?.let { it1 ->
                            UserModel(uid,enteredFirstName,enteredLastName,enteredNumber,
                                it1
                            )
                        }

                        if(uid != null){
                            if (user != null) {
                                fireStore.collection("User").document(uid).set(user)
                            }
                        }
                        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            Toast.makeText(this,"Please verify your email!", Toast.LENGTH_LONG).show()

                        }
                        ?.addOnFailureListener{
                                Toast.makeText(this,"Something wrong happened, try again!", Toast.LENGTH_LONG).show()

                            }

                        val intent = Intent(this,NavigationActivity::class.java)
                        startActivity(intent)
                        finish()



                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Log.e("AuthError","Failed",task.exception)
                    }
                }

            }
        headToLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)



        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_CODE_REQ){
                val bitmap = data?.extras?.get("data") as Bitmap?
                imageUploaded?.setImageBitmap(bitmap)

                userImageBase64Converted = bitmap?.let { encodeBitmapToBase64(it) }

            }

            else if(requestCode == UPLOAD_IMAGE_CODE_REQ){
                imageUploaded?.setImageURI(data?.data)
                userImageBase64Converted = data?.data?.let{encodeUriToBase64(it)}


            }
        }
    }

    private fun encodeUriToBase64(uri: Uri):String {
        val inputStream = contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes()
        return byteArray?.let { Base64.encodeToString(it, Base64.DEFAULT) } ?: ""
    }

    private fun encodeBitmapToBase64(bitmap: Bitmap):String {
        var byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray,Base64.DEFAULT)
    }
}







