package com.example.googlemapsapplication.data

class LocalStorage private constructor(){
    var mapDao = LocalMapDao()
        private set

    companion object {
        @Volatile private var instance: LocalStorage? = null

        fun getInstance()=
            instance ?: synchronized(this){
                instance ?:LocalStorage().also{instance = it}
            }
    }
}