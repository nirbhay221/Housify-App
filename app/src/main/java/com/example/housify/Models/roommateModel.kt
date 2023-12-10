package com.example.housify.Models

import com.example.housify.UserModel

class roommateModel {
    var roommateGroupId: String ?= ""
        get() = field?:""
        set(value) {
            field = value ?: ""
        }

    var roommateGroupName: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }

    var roommates: MutableList<UserModel> = mutableListOf()

    var tasks: MutableList<TaskModel> = mutableListOf()
    constructor(roomId: String?, roomName: String?, roommates: MutableList<UserModel>) {
        this.roommateGroupId = roomId
        this.roommateGroupName = roomName
        this.roommates = roommates
    }

    constructor() : this("", "", mutableListOf())
}