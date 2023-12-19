package com.example.housify.Models

import com.example.housify.UserModel

//  Defining the roommate model for the roommate group living in the house

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