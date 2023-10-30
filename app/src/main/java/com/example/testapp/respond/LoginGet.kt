package com.example.testapp.respond

import com.google.gson.annotations.SerializedName

class LoginGet {
    @SerializedName("jwt")
    var jwt : String = ""
}