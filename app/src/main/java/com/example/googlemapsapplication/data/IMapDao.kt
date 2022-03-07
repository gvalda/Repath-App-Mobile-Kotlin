package com.example.googlemapsapplication.data

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.Marker


interface IMapDao {
    fun addMarker(marker:Marker);
    fun getMarkers() : LiveData<List<Marker>>;
}