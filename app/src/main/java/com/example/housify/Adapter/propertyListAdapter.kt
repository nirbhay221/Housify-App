package com.example.housify.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Models.propertyModel
import com.example.housify.R

class propertyListAdapter(private val propertyList: ArrayList<propertyModel>): RecyclerView.Adapter<propertyListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): propertyListAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.property_item_view,parent,false)
        return MyViewHolder(itemView)
            }

    override fun onBindViewHolder(holder: propertyListAdapter.MyViewHolder, position: Int) {
        val property: propertyModel = propertyList[position]
        holder.propertyTitle.text = property.propertTitle
        holder.propertyPrice.text = property.propertyPrice
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    public class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val propertyTitle : TextView = itemView.findViewById(R.id.apartmentTitleName)
        val propertyPrice : TextView = itemView.findViewById(R.id.apartmentPrice)

    }
}