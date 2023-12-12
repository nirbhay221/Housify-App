package com.example.housify.Adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.propertyInfoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class propertyLikedFilterAdapter (private val propertyList: ArrayList<propertyModel>): RecyclerView.Adapter<propertyLikedFilterAdapter.MyPropertyLikedViewHolder>() {


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var isPropertyLiked: Boolean = false

    var currentUserUid = auth.currentUser?.uid
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): propertyLikedFilterAdapter.MyPropertyLikedViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.property_posted_item_view, parent, false)

        return MyPropertyLikedViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: propertyLikedFilterAdapter.MyPropertyLikedViewHolder, position: Int) {
        val property: propertyModel = propertyList[position]
        if (currentUserUid != null) {

            firestore.collection("User").document(currentUserUid!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    val likedProperties =
                        documentSnapshot["likedProperties"] as? List<String> ?: emptyList()
                    isPropertyLiked = likedProperties.contains(property.propertyUid)
                    holder.likeButton.setImageResource(
                        if (isPropertyLiked)
                            R.drawable.ic_like_selected
                        else
                            R.drawable.ic_like
                    )
                }
                .addOnFailureListener {

                }
        }
        holder.propertyTitle.text = property.propertyTitle
        holder.propertyAddress.text = property.propertyAddress
        holder.propertyTitle.setOnClickListener {
            holder.propertyTitle.text = property.userUid
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


        holder.viewMoreInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("propertyName", property.propertyTitle)
            bundle.putString("propertyLocation", property.propertyAddress)

            bundle.putString("propertyUid", property.userUid)
            val intent = Intent(holder.itemView.context, propertyInfoActivity::class.java)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)

        }


        holder.likeButton.setOnClickListener {
            if (currentUserUid != null) {

                firestore.collection("User").document(currentUserUid).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val likedProperties =
                            documentSnapshot["likedProperties"] as? List<String> ?: emptyList()
                        isPropertyLiked = likedProperties.contains(property.propertyUid)
                        holder.likeButton.setImageResource(
                            if (isPropertyLiked)
                                R.drawable.ic_like_selected
                            else
                                R.drawable.ic_like
                        )
                    }
                    .addOnFailureListener {

                    }
            }
            Toast.makeText(holder.itemView.context, "$isPropertyLiked", Toast.LENGTH_LONG).show()
            if (!isPropertyLiked) {
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
            } else {
                var currentUserId = auth.currentUser?.uid
                property.propertyTotalLikes = property.propertyTotalLikes?.minus(1)
                property.propertyUid?.let { it1 ->
                    firestore.collection("Properties").document(it1)
                        .update("propertyTotalLikes", property.propertyTotalLikes)
                        .addOnSuccessListener {
                            auth.currentUser?.uid.let { userUid ->
                                if (userUid != null) {
                                    firestore.collection("User").document(userUid)
                                        .update("likedProperties", FieldValue.arrayRemove(it1))
                                        .addOnSuccessListener {
                                            isPropertyLiked = false
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

    public class MyPropertyLikedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val propertyTitle: TextView = itemView.findViewById(R.id.apartmentPostedTitleName)
        val propertyAddress: TextView = itemView.findViewById(R.id.apartmentPostedLocation)
        var viewMoreInfo = itemView.findViewById<TextView>(R.id.viewPostedApartmentInfo)
        var likeButton = itemView.findViewById<ImageView>(R.id.likeButton)



    }
}

