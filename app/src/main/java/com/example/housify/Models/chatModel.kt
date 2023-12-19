package com.example.housify.Models

import com.example.housify.UserModel
import com.google.firebase.Timestamp

//  Defining the chat model for sender, receiver, message and timestamp

class chatModel {
        var senderUid : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }


    var receiverUid : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
        var message : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
        var timestamp: Timestamp? = null
            get() = field
            set(value) {
                field = value
            }


    constructor()
    constructor(
        senderUid: String?,
        receiverUid: String?,
        message: String?,
        timestamp: Timestamp?

        ) {
        this.senderUid = senderUid
        this.receiverUid= receiverUid
        this.message = message
        this.timestamp =  timestamp

    }



}