package com.example.housify.Models

import com.example.housify.UserModel

class propertyModel {
    var propertyTitle : String? = ""
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
    var propertyYear : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }

    var propertyArea : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var propertyRentMonthlyOrAnnually : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }

    var propertyBedrooms : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }

    var propertyBathrooms : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var propertyTotalRoommatesNeeded : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }

    var propertyApprovalRented: MutableList<Pair<UserModel,String>> = mutableListOf()

    var propertyFacilities: MutableList<String> = mutableListOf()

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
    var propertyUid : String?= ""
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
        userPropertyImages: String?,
        propertyTotalLikes: Int?,
        propertyUid: String?,
        propertyYear: String?,
        propertyArea: String?,
        propertyRentMonthlyOrAnnually: String?,
        propertyBedrooms: String?,
        propertyBathrooms: String?,
        propertyTotalRoommatesNeeded: String?
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
        this.propertyUid = propertyUid
        this.propertyYear = propertyYear
        this.propertyArea = propertyArea
        this.propertyRentMonthlyOrAnnually = propertyRentMonthlyOrAnnually
        this.propertyBedrooms = propertyBedrooms
        this.propertyBathrooms = propertyBathrooms
        this.propertyTotalRoommatesNeeded = propertyTotalRoommatesNeeded
    }


    constructor(
        propertyTitle: String?,
        propertyType: String?,
        propertyAddress: String?,
        propertyPasscode: String?,
        propertyPrice: String?,
        propertyDescription: String?,
        userUid: String?,
        userPropertyImages: String?,
        propertyTotalLikes: Int?,
        propertyUid: String?,
        propertyYear: String?,
        propertyArea: String?,
        propertyRentMonthlyOrAnnually: String?,
        propertyBedrooms: String?,
        propertyBathrooms: String?,
        propertyTotalRoommatesNeeded: String?,
        propertyFacilities: MutableList<String>
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
        this.propertyUid = propertyUid
        this.propertyYear = propertyYear
        this.propertyArea = propertyArea
        this.propertyRentMonthlyOrAnnually = propertyRentMonthlyOrAnnually
        this.propertyBedrooms = propertyBedrooms
        this.propertyBathrooms = propertyBathrooms
        this.propertyTotalRoommatesNeeded = propertyTotalRoommatesNeeded
        this.propertyFacilities = propertyFacilities
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
    constructor(propertyTitle: String, propertyType: String, propertyAddress: String, propertyPasscode: String, propertyPrice: String, propertyDescription: String, currentUserUid: String,propertyYear: String?,
                propertyArea: String?,
                propertyRentMonthlyOrAnnually: String?,
                propertyBedrooms: String?,
                propertyBathrooms: String?,
                propertyTotalRoommatesNeeded: String?,
                propertyTotalFacility:MutableList<String>)
    {
        this.propertyTitle = propertyTitle
        this.propertyType = propertyType
        this.propertyAddress = propertyAddress
        this.propertyPasscode = propertyPasscode
        this.propertyPrice = propertyPrice
        this.propertyDescription = propertyDescription
        this.userUid = currentUserUid
        this.propertyYear = propertyYear
        this.propertyArea = propertyArea
        this.propertyRentMonthlyOrAnnually = propertyRentMonthlyOrAnnually
        this.propertyBedrooms = propertyBedrooms
        this.propertyBathrooms = propertyBathrooms
        this.propertyTotalRoommatesNeeded = propertyTotalRoommatesNeeded
        this.propertyFacilities = propertyTotalFacility
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