package com.example.googlemapsapplication.data.responses

import com.example.googlemapsapplication.data.models.User
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("code")
    var statusCode: Int,

    @SerializedName("access")
    var authToken: String,

    @SerializedName("user")
    var user: User?
)