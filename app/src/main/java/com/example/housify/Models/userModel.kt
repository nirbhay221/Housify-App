package com.example.housify

class userModel {
    var firstName : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var lastName : String? = ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var number : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }
    var userImage : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }

    var likedProperties: MutableList<String> = mutableListOf()
    constructor():this("","","","")
    constructor(firstName: String?, lastName: String?, number: String?, userImage: String?, likedProperties: MutableList<String>) {
        this.firstName = firstName
        this.lastName = lastName
        this.number = number
        this.userImage = userImage
        this.likedProperties = likedProperties
    }

    constructor(firstName:String?,lastName:String?,number:String?,userImage:String?){
        this.firstName = firstName
        this.lastName = lastName
        this.number = number
        this.userImage = userImage

    }


}