package com.example.housify.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.housify.HomeActivity
import com.example.housify.LoginActivity
import com.example.housify.PropertyAddViewActivity
import com.example.housify.R
import com.example.housify.databinding.FragmentHomeBinding
import com.example.housify.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class profileFragment: Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var user = auth.currentUser
        var userMail = user?.email
        binding.profileUserMailInfo.text = userMail
        val uid = auth.currentUser?.uid
        uid?.let{
            userId->
                FirebaseFirestore.getInstance().collection("User").document(userId).get().addOnSuccessListener { document ->
                    if(document != null) {
                        val firstName = document.getString("firstName")
                        binding.profileUserNameInfo.text = "$firstName"
                        var userImageBase64 = document.getString("userImage")

                        if(!userImageBase64.isNullOrBlank()){
                            var decodedImage = decodeBase64ToBitmap(userImageBase64)
                            binding.userProfileImage.setImageBitmap(decodedImage)
                        }
                    }
                    else{
                        Log.d("Profile Fragment","User details not found")
                    }

                }.addOnFailureListener{
                    exception ->
                    Log.e("Profile Fragment","Error getting user details",exception)
                }
            binding.fourthLayout.setOnClickListener{

                val intent = Intent(requireContext(),PropertyAddViewActivity::class.java)
                startActivity(intent)
            }
            binding.secondLayout.setOnClickListener{

                val intent = Intent(requireContext(),HomeActivity::class.java)
                startActivity(intent)
            }
        }

        binding.profileLogoutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

    }
    private fun decodeBase64ToBitmap(base64:String):Bitmap{
        var decodeByteArray = Base64.decode(base64,Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodeByteArray,0,decodeByteArray.size)
    }

}