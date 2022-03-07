package com.example.googlemapsapplication.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.Marker

class LocalMapDao:IMapDao {
    private var markerList = mutableListOf<Marker>()
    private val markers = MutableLiveData<List<Marker>>()

    init{
        markers.value = markerList
    }

    override fun addMarker(marker: Marker) {
        markerList.add(marker)
        markers.value = markerList
    }

    override fun getMarkers() = markers as LiveData<List<Marker>>
}