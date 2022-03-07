package com.example.googlemapsapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googlemapsapplication.data.MapRepository

class MapsViewModelFactory (private val mapRepository: MapRepository):ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapsViewModel(mapRepository) as T
    }
}