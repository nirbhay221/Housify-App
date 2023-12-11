package com.example.housify.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Models.TaskModel
import com.example.housify.Models.propertyModel
import com.example.housify.R

class taskPostedAdapter(private val taskList: ArrayList<TaskModel>): RecyclerView.Adapter<taskPostedAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): taskPostedAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item_card,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: taskPostedAdapter.MyViewHolder, position: Int) {
        val task: TaskModel = taskList[position]
        holder.taskName.text = task.taskTitle
        holder.taskDeadline.text = task.taskDeadline
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    public class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var taskName : TextView = itemView.findViewById(R.id.taskNameListed)
        var taskDeadline : TextView = itemView.findViewById(R.id.taskdeadlineAssigned)

    }
}