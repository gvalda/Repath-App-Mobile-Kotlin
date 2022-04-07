package com.example.googlemapsapplication.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.googlemapsapplication.R

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object{
        const val USER_TOKEN = "user_token"
    }

    fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        Log.d("myTag", USER_TOKEN)

        editor.apply()
    }
    
    fun fetchAuthToken(): String?{
        Log.d("myTag", prefs.getString(USER_TOKEN, null).toString())

        return prefs.getString(USER_TOKEN, null)
    }

    fun clearAuthToken(){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, null)
        editor.apply()
    }
}