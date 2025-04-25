package com.example.hotelfinder.data.local

import androidx.room.TypeConverter
import com.example.hotelfinder.data.remote.model.Geometry
import com.example.hotelfinder.data.remote.model.Photo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromGeometry(geometry: Geometry): String{
        return Gson().toJson(geometry)
    }
    @TypeConverter
    fun toGeometry(geometryString: String): Geometry {
        val type = object : TypeToken<Geometry>() {}.type
        return Gson().fromJson(geometryString, type)
    }

    // Converte una lista di Photo in una stringa JSON
    @TypeConverter
    fun fromPhotoList(photos: List<Photo>): String {
        return Gson().toJson(photos)
    }

    // Converte una stringa JSON in una lista di Photo
    @TypeConverter
    fun toPhotoList(photosString: String): List<Photo> {
        val type = object : TypeToken<List<Photo>>() {}.type
        return Gson().fromJson(photosString, type)
    }
}