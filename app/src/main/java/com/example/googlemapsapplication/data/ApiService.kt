package com.example.googlemapsapplication.data

import com.example.googlemapsapplication.data.requests.LoginRequest
import com.example.googlemapsapplication.data.responses.LoginResponse
import com.example.googlemapsapplication.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}