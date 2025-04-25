package com.example.hotelfinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hotelfinder.data.local.Converters
import com.example.hotelfinder.data.remote.model.Geometry
import com.example.hotelfinder.data.remote.model.Photo


/* usiamo la libreria room per creare un database, qui stiamo creando un database chiamato hotels */
@Entity(tableName = "hotels")
@TypeConverters(Converters::class) // Per salvare liste e oggetti complessi perche room non è in grado di farlo da solo, allora usa la classe converters
data class LocalHotel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val indirizzo: String,
    val classificazione: Double?,
    val denominazione: String,
    val foto: List<Photo>?,
    val posizione: Geometry,
    val icon: String,
    val iconBackgroundColor: String,
    val iconMaskBaseUri: String,
    val placeId: String
)
