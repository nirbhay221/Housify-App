package com.example.housify.Models

class transactionModel {
    var transactionId : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var transactionTitle : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var transactionType : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var transactionAmount : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var transactionTime : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var fromUser : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var toUser : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }


    constructor(
        transactionId: String?,
        transactionTitle: String?,
        transactionType: String?,
        transactionAmount: String?,
        transactionTime: String?,
        fromUser: String?,
        toUser: String?,
    ) {
        this.transactionId = transactionId
        this.transactionTitle = transactionTitle
        this.transactionType = transactionType
        this.transactionAmount = transactionAmount
        this.transactionTime = transactionTime
        this.fromUser = fromUser
        this.toUser = toUser
    }
    constructor():this("","","","","","","")

}