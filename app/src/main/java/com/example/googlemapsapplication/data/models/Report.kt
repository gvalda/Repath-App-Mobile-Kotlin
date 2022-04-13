package com.example.googlemapsapplication.data.models

import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("location")
    var location: String,

    @SerializedName("author")
    var author: User,

    @SerializedName("type")
    var type: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("obstacle_photo")
    var photo: Int,
)