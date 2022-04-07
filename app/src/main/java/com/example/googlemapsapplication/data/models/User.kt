package com.example.googlemapsapplication.data.models

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("username")
    var username: String,

    @SerializedName("email")
    var email: String
)