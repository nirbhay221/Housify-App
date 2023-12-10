package com.example.housify.Models

import com.example.housify.UserModel

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

    var assignedUsers: MutableList<UserTaskAssignment> = mutableListOf()

    var assignedToCurrentUser: Boolean = false
    constructor()
    constructor(
        taskId: String?,
        taskTitle: String?,
        taskDescription: String?,
        taskDeadline: String?,
        assignedUsers: MutableList<UserTaskAssignment>,
        assignedToCurrentUser: Boolean
    ) {
        this.taskId = taskId
        this.taskTitle = taskTitle
        this.taskDescription = taskDescription
        this.taskDeadline = taskDeadline
        this.assignedUsers = assignedUsers
        this.assignedToCurrentUser = assignedToCurrentUser
    }
    constructor(
        taskTitle: String?,
        taskDescription: String?,
        taskDeadline: String?
    ) {
        this.taskTitle = taskTitle
        this.taskDescription = taskDescription
        this.taskDeadline = taskDeadline
    }
    constructor(nothing: Nothing?, taskTitle: String, taskDescription: String, taskDeadline: String, nothing1: Nothing?)

}
class UserTaskAssignment(
    var user: UserModel? = null,
    var scheduledCompletionTime: String? = ""

)