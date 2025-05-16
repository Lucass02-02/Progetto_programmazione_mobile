package com.example.hotelfinder.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.hotelfinder.data.local.entity.LocalHotel
import kotlinx.coroutines.flow.Flow


@Dao
interface HotelDao {

    //@Upsert serve per inserire uno quando non esiste e aggiornarlo se invece esiste gia, qui stiamo inserendo un solo hotel
    @Upsert
    suspend fun insert(hotel: LocalHotel)

    @Upsert
    suspend fun insert(hotel: List<LocalHotel>)

    @Query("SELECT * FROM hotels GROUP BY  denominazione ")
    fun getAll(): Flow<List<LocalHotel>>  //la get non va in suspend perche ritorna un flow

    @Query("DELETE FROM hotels")
    suspend fun deleteAll()



    @Query("SELECT * FROM hotels WHERE placeId = :placeId AND indirizzo = :indirizzo AND denominazione = :nome GROUP BY  denominazione ")
    fun getHotelByAddress(placeId: String,indirizzo: String, nome: String): Flow<List<LocalHotel>>
}