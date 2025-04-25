package com.example.hotelfinder.domain.repository

import com.example.hotelfinder.domain.model.Hotel
import kotlinx.coroutines.flow.Flow

interface HotelLocalRepository {

    suspend fun insert(hotel: Hotel)
    suspend fun insert(hotels: List<Hotel>)
    fun getAll(): Flow<List<Hotel>>
    suspend fun clearAll()

    //per la detail activity
    fun getHotelByAddress(placeId: String, indirizzo: String, nome: String): Flow<List<Hotel>>
}