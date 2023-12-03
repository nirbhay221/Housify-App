package com.example.housify.Models

class messageModel {
    var messageContent:String? = ""
        get() = field?: ""
        set(value){
            field = value ?: ""
        }
    var senderUid:String? = ""
        get() = field?: ""
        set(value){
            field = value ?: ""
        }
    var receiverUid:String? = ""
    get() = field?: ""
    set(value){
        field = value ?: ""
    }
    var timestamp:String? = ""
    get() = field?: ""
    set(value){
        field = value ?: ""
    }

    constructor(messageContent:String? , senderUid:String?,
        receiverUid:String? , timestamp:String?){
        this.messageContent = messageContent
        this.senderUid = senderUid
        this.receiverUid = receiverUid
        this.timestamp = timestamp
    }
    constructor():this("","","","")

}