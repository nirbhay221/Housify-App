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
        propertyTitle: String?,
        propertyType: String?,
        propertyAddress: String?,
        propertyPasscode: String?,
        propertyPrice: String?,
        propertyDescription: String?,
        userUid: String?,
        userPropertyImages:String?,
        propertyTotalLikes : Int?
    ) {
        this.propertyTitle = propertyTitle
        this.propertyType = propertyType
        this.propertyAddress = propertyAddress
        this.propertyPasscode = propertyPasscode
        this.propertyPrice = propertyPrice
        this.propertyDescription = propertyDescription
        this.userPropertyImages = userPropertyImages
        this.userUid = userUid
        this.propertyTotalLikes = propertyTotalLikes
    }
    constructor(
        propertyTitle: String?,
        propertyType: String?,
        propertyAddress: String?,
        propertyPasscode: String?,
        propertyPrice: String?,
        propertyDescription: String?,
        userUid: String?,
        userPropertyImages:String?
    ) {
        this.propertyTitle = propertyTitle
        this.propertyType = propertyType
        this.propertyAddress = propertyAddress
        this.propertyPasscode = propertyPasscode
        this.propertyPrice = propertyPrice
        this.propertyDescription = propertyDescription
        this.userPropertyImages = userPropertyImages
        this.userUid = userUid
    }
    constructor():this("","","","","","","","")
    constructor(propertyTitle: String, propertyType: String, propertyAddress: String, propertyPasscode: String, propertyPrice: String, propertyDescription: String, currentUserUid: String)
    {
        this.propertyTitle = propertyTitle
        this.propertyType = propertyType
        this.propertyAddress = propertyAddress
        this.propertyPasscode = propertyPasscode
        this.propertyPrice = propertyPrice
        this.propertyDescription = propertyDescription
        this.userUid = currentUserUid
    }
    constructor(propertyTitle: String, propertyType: String, propertyAddress: String, propertyPasscode: String, propertyPrice: String, propertyDescription: String, currentUserUid: String,propertyTotalLikes: Int?,propertyUid:String)
    {
        this.propertyTitle = propertyTitle
        this.propertyType = propertyType
        this.propertyAddress = propertyAddress
        this.propertyPasscode = propertyPasscode
        this.propertyPrice = propertyPrice
        this.propertyDescription = propertyDescription
        this.userUid = currentUserUid
        this.propertyTotalLikes = propertyTotalLikes
        this.propertyUid = propertyUid
    }
}