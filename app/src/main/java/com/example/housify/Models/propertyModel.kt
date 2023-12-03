package com.example.housify.Models

class propertyModel {
    var propertTitle : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var propertyType : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var propertyAddress : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var propertyPasscode : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var propertyPrice : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var propertyDescription : String?= ""
    get() = field ?: ""
    set(value){
        field = value ?: ""
    }
    var userUid : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }

    var userPropertyImages : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }

    var propertyTotalLikes:Int ?= 0
        get() = field?: 0
        set(value){
            field = value ?:0
        }



    constructor(
        propertTitle: String?,
        propertyType: String?,
        propertyAddress: String?,
        propertyPasscode: String?,
        propertyPrice: String?,
        propertyDescription: String?,
        userUid: String?,
        userPropertyImages:String?
    ) {
        this.propertTitle = propertTitle
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
        this.propertTitle = propertTitle
        this.propertyType = propertyType
        this.propertyAddress = propertyAddress
        this.propertyPasscode = propertyPasscode
        this.propertyPrice = propertyPrice
        this.propertyDescription = propertyDescription
        this.userUid = userUid
    }
}