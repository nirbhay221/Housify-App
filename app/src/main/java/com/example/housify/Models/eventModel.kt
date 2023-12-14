package com.example.housify.Models

import com.example.housify.UserModel


class eventModel {
    var eventId: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }

    var eventTitle: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }

    var eventDescription: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }

    var eventRoommateId: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }
    var assignedUsers: MutableList<Pair<UserModel,String>> = mutableListOf()

    constructor()
    constructor(
        eventId: String?,
        eventTitle: String?,
        eventDescription: String?,
        eventRoommateId: String?,
        assignedUsers: MutableList<Pair<UserModel,String>>,

        ) {
        this.eventId = eventId
        this.eventTitle = eventTitle
        this.eventDescription = eventDescription
        this.eventRoommateId = eventRoommateId
        this.assignedUsers = assignedUsers
    }
    constructor(
        eventId: String?,
        eventTitle: String?,
        eventDescription: String?,
        eventRoommateId: String?)
    {
        this.eventId = eventId
        this.eventTitle = eventTitle
        this.eventDescription = eventDescription
        this.eventRoommateId = eventRoommateId
    }


}