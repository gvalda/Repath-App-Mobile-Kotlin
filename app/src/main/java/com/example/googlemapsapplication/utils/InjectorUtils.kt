package com.example.googlemapsapplication.utils

import com.example.googlemapsapplication.data.LocalMapDao
import com.example.googlemapsapplication.data.LocalStorage
import com.example.googlemapsapplication.data.MapRepository
import com.example.googlemapsapplication.ui.MapsViewModelFactory

object InjectorUtils {
    fun provideMapsViewModelFactory(): MapsViewModelFactory{
        val mapRepository = MapRepository.getInstance(LocalStorage.getInstance().mapDao)
        return MapsViewModelFactory(mapRepository)
    }

    fun mapRepository() : MapRepository{
        return MapRepository.getInstance(LocalStorage.getInstance().mapDao)
    }
}