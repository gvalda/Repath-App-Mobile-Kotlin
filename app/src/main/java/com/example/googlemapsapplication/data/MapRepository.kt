package com.example.googlemapsapplication.data

import com.google.android.gms.maps.model.Marker

class MapRepository private constructor(private val mapDao:IMapDao){
    fun addMarker(marker:Marker){
        mapDao.addMarker(marker);
    }

    fun getMarkers() = mapDao.getMarkers()

    companion object{
        @Volatile private var instance: MapRepository? = null

        fun getInstance(mapDao: IMapDao)= instance ?: synchronized(this){
            instance ?: MapRepository(mapDao).also { instance = it }
        }
    }
}