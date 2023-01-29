package com.konge.dolbogram.models

data class UserModel (
    val id:String = "",
    var username:String = "",
    var bio:String = "",
    var fullname:String = "",
    var state:String = "",
    var photoUrl:String = "empty",
    var phone:String = "",
    var messaging_token:String = ""
    )