package com.example.housify

class UserModel {
    var uid: String? = ""
        get() = field ?: ""
        set(value) {
            field = value ?: ""
        }
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
    var userLocation : String?= ""
        get() = field ?: ""
        set(value){
            field = value ?: ""
        }

    constructor():this("","","","")

    constructor(firstName:String?,lastName:String?,number:String?,userImage:String){
        this.firstName = firstName
        this.lastName = lastName
        this.number = number
        this.userImage = userImage
    }
    constructor(userUid:String?,firstName:String?,lastName:String?,number:String?,userImage:String){
        this.firstName = firstName
        this.lastName = lastName
        this.number = number
        this.userImage = userImage
        this.uid= userUid
    }

    constructor(enteredFirstName: String, enteredLastName: String, enteredNumber: String)
    {
        this.firstName = firstName
        this.lastName = lastName
        this.number = number
    }

    constructor(currentUserUid: String)

}