package com.example.housify.Adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.LoginActivity
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.propertyInfoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class propertyPostedAdapter (private val propertyList: ArrayList<propertyModel>): RecyclerView.Adapter<propertyPostedAdapter.MyViewHolder>() {


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var isPropertyLiked:Boolean = false

    var currentUserUid =auth.currentUser?.uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): propertyPostedAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.property_posted_item_view,parent,false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: propertyPostedAdapter.MyViewHolder, position: Int) {
        val property: propertyModel = propertyList[position]
        if (currentUserUid != null) {

            firestore.collection("User").document(currentUserUid!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    val likedProperties = documentSnapshot["likedProperties"] as? List<String> ?: emptyList()
                    isPropertyLiked = likedProperties.contains(property.propertyUid)
                }
                .addOnFailureListener {

                }
        }

        holder.propertyType.text  = property.propertyType
        holder.propertyTitle.text = property.propertyTitle
        holder.propertyAddress.text = property.propertyAddress
        holder.propertyPrice.text ="$ "+ property.propertyPrice
        holder.propertyTitle.setOnClickListener{
            holder.propertyTitle.text = property.userUid
        }

        if (!property.userPropertyImages.isNullOrEmpty()) {
            val decodedString: ByteArray = Base64.decode(property.userPropertyImages, Base64.DEFAULT)
            val decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            holder.propertyImage.setImageBitmap(decodedBitmap)
        } else {

        }
        val currentUserUid = auth.currentUser?.uid
//        if (currentUserUid != null) {
//            firestore.collection("User").document(currentUserUid).get()
//                .addOnSuccessListener { documentSnapshot ->
//                    val likedProperties = documentSnapshot["likedProperties"] as? List<String> ?: emptyList()
//                    isPropertyLiked = likedProperties.contains(property.propertyUid)
//                }
//                .addOnFailureListener {
//                }
//        }


        holder.viewMoreInfo.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("propertyName", property.propertyTitle)
            bundle.putString("propertyLocation", property.propertyAddress)
            bundle.putString("propertyUid", property.propertyUid)
            bundle.putString("userUid", property.userUid)
            val intent = Intent(holder.itemView.context, propertyInfoActivity::class.java)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)

        }


        holder.likeButton.setOnClickListener{
            if (currentUserUid != null) {

                firestore.collection("User").document(currentUserUid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val likedProperties = documentSnapshot["likedProperties"] as? List<String> ?: emptyList()
                        isPropertyLiked = likedProperties.contains(property.propertyUid)
                    }
                    .addOnFailureListener {

                    }
            }
            Toast.makeText(holder.itemView.context,"$isPropertyLiked",Toast.LENGTH_LONG).show()
            if(!isPropertyLiked){
                var currentUserId = auth.currentUser?.uid
                property.propertyTotalLikes = property.propertyTotalLikes?.plus(1)
                property.propertyUid?.let { it1 ->
                    firestore.collection("Properties").document(it1)
                        .update("propertyTotalLikes", property.propertyTotalLikes)
                        .addOnSuccessListener {
                            auth.currentUser?.uid.let { userUid ->
                                if (userUid != null) {
                                    firestore.collection("User").document(userUid)
                                        .update("likedProperties", FieldValue.arrayUnion(it1))
                                        .addOnSuccessListener {
                                            isPropertyLiked = true
                                            holder.updateLikeButton(isPropertyLiked)
                                        }
                                        .addOnFailureListener {}
                                }
                            }
                        }.addOnFailureListener {

                        }
                }
                if (currentUserId != null) {
                    //firestore.collection("User").document(currentUserId).update("likedProperties",)
                }
            }
            else{
                var currentUserId = auth.currentUser?.uid
                property.propertyTotalLikes = property.propertyTotalLikes?.minus(1)
                property.propertyUid?.let { it1 ->
                    firestore.collection("Properties").document(it1).update("propertyTotalLikes", property.propertyTotalLikes)
                        .addOnSuccessListener {
                            auth.currentUser?.uid.let { userUid ->
                                if (userUid != null) {
                                    firestore.collection("User").
                                    document(userUid).update("likedProperties", FieldValue.arrayRemove(it1))
                                        .addOnSuccessListener {
                                            isPropertyLiked = false
                                            holder.updateLikeButton(isPropertyLiked)
                                        }
                                        .addOnFailureListener { }
                                }
                            }
                        }
                        .addOnFailureListener { }
                }
            }

        }
    }


    override fun getItemCount(): Int {
        return propertyList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val propertyType : TextView = itemView.findViewById(R.id.propertyPostedType)
        val propertyTitle : TextView = itemView.findViewById(R.id.apartmentPostedTitleName)
        val propertyAddress : TextView = itemView.findViewById(R.id.apartmentPostedLocation)
        val propertyPrice : TextView = itemView.findViewById(R.id.propertyPostedApartmentPrice)
        var viewMoreInfo = itemView.findViewById<TextView>(R.id.viewPostedApartmentInfo)
        var likeButton = itemView.findViewById<ImageView>(R.id.likeButton)
        val propertyImage: ImageView = itemView.findViewById(R.id.propertyPostedImage)

        fun updateLikeButton(isLiked: Boolean) {
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_like_selected)
            } else {
                likeButton.setImageResource(R.drawable.ic_like)
            }
        }



    }

}