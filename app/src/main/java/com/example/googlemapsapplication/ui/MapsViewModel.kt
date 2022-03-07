package com.example.googlemapsapplication.ui

import androidx.lifecycle.ViewModel
import com.example.googlemapsapplication.data.MapRepository
import com.google.android.gms.maps.model.Marker

class MapsViewModel(private val mapRepository: MapRepository):ViewModel() {
    fun getMarkers() = mapRepository.getMarkers()

    fun addMarker(marker:Marker) = mapRepository.addMarker(marker)
}