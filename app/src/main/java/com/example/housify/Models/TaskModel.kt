package com.example.housify.Models

import com.example.housify.UserModel
import com.google.firebase.firestore.auth.User

class TaskModel {
    var taskId: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }

    var taskTitle: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }

    var taskDescription: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }

    var taskDeadline: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }
    var taskRoommateId: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }
    var assignedUsers: MutableList<Pair<UserModel,String>> = mutableListOf()

    var assignedToCurrentUser: Boolean = false
    constructor()
    constructor(
        taskId: String?,
        taskTitle: String?,
        taskDescription: String?,
        taskDeadline: String?,
        taskRoommateId: String?,
        assignedUsers: MutableList<Pair<UserModel,String>>,

    ) {
        this.taskId = taskId
        this.taskTitle = taskTitle
        this.taskDescription = taskDescription
        this.taskDeadline = taskDeadline
        this.taskRoommateId = taskRoommateId
        this.assignedUsers = assignedUsers
    }
    constructor(
        taskId: String?,
        taskTitle: String?,
        taskDescription: String?,
        taskDeadline: String?,

        taskRoommateId: String?,

        ) {
        this.taskId = taskId
        this.taskTitle = taskTitle
        this.taskDescription = taskDescription
        this.taskDeadline = taskDeadline
        this.taskRoommateId = taskRoommateId
    }
    constructor(
        taskId: String?,
        taskTitle: String?,
        taskDescription: String?,
        taskDeadline: String?,
        taskRoommateId: String?,
        assignedUsers: MutableList<Pair<UserModel,String>>,
        assignedToCurrentUser: Boolean

        ) {
        this.taskId = taskId
        this.taskTitle = taskTitle
        this.taskDescription = taskDescription
        this.taskDeadline = taskDeadline
        this.assignedUsers = assignedUsers
        this.taskRoommateId = taskRoommateId
        this.assignedToCurrentUser = assignedToCurrentUser
    }
    constructor(
        taskTitle: String?,
        taskDescription: String?,
        taskDeadline: String?,
        taskRoommateId: String?
    ) {
        this.taskTitle = taskTitle
        this.taskDescription = taskDescription
        this.taskDeadline = taskDeadline
        this.taskRoommateId
    }

}
//class UserTaskAssignment(
//    var user: UserModel? = null,
//    var scheduledCompletionTime: String? = ""
//
//)