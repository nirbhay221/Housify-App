package com.example.housify.Adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.LoginActivity
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.propertyInfoActivity

class propertyPostedAdapter (private val propertyList: ArrayList<propertyModel>): RecyclerView.Adapter<propertyPostedAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): propertyPostedAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.property_posted_item_view,parent,false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: propertyPostedAdapter.MyViewHolder, position: Int) {
        val property: propertyModel = propertyList[position]
        holder.propertyTitle.text = property.propertyTitle
        holder.propertyAddress.text = property.propertyAddress

        holder.viewMoreInfo.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("propertyName", property.propertyTitle)
            bundle.putString("propertyLocation", property.propertyAddress)

            bundle.putString("propertyUid", property.userUid)
            val intent = Intent(holder.itemView.context, propertyInfoActivity::class.java)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)

        }
        holder.likeButton.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val propertyTitle : TextView = itemView.findViewById(R.id.apartmentPostedTitleName)
        val propertyAddress : TextView = itemView.findViewById(R.id.apartmentPostedLocation)
        var viewMoreInfo = itemView.findViewById<TextView>(R.id.viewPostedApartmentInfo)
        var likeButton = itemView.findViewById<ImageView>(R.id.likeButton)



        }

    }
