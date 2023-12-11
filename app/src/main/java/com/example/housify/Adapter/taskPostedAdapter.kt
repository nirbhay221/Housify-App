package com.example.housify.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.util.Base64
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.RecyclerView
import com.example.housify.Models.TaskModel
import com.example.housify.Models.propertyModel
import com.example.housify.R
import com.example.housify.assignUserToTask
import com.example.housify.fragments.personalStatsFragment
import com.example.housify.propertyInfoActivity

class taskPostedAdapter(private val taskList: ArrayList<TaskModel>): RecyclerView.Adapter<taskPostedAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): taskPostedAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item_card,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: taskPostedAdapter.MyViewHolder, position: Int) {
        val task: TaskModel = taskList[position]
        holder.taskName.text = task.taskTitle
        holder.taskDeadline.text = task.taskDeadline
//        holder.addButton.setOnClickListener{
//
//            var addFragment = personalStatsFragment().apply {
//                arguments = Bundle().apply{
//                    putParcelable("task",task as Parcelable)
//                }
//            }
//            val transaction = (holder.itemView.context as AppCompatActivity).supportFragmentManager
//                .beginTransaction()
//            transaction.replace(R.id.fragment_tasks,addFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//
//        }
        holder.addUserToTask.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("taskName", task.taskTitle)
            bundle.putString("taskDeadline", task.taskDeadline)
            bundle.putString("taskId", task.taskId)
            bundle.putString("taskDescription", task.taskDescription)
            val intent = Intent(holder.itemView.context,assignUserToTask::class.java)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    public class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var taskName : TextView = itemView.findViewById(R.id.taskNameListed)
        var taskDeadline : TextView = itemView.findViewById(R.id.taskdeadlineAssigned)
        var addUserToTask :ImageButton = itemView.findViewById(R.id.addButtonUserToTask)

    }
}