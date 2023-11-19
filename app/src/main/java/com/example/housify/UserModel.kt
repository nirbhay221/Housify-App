package com.example.housify

class UserModel {
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
    constructor():this("","","")

    constructor(firstName:String?,lastName:String?,number:String?){
        this.firstName = firstName
        this.lastName = lastName
        this.number = number
    }


}