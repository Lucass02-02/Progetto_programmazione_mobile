package com.example.hotelfinder.data.remote

import com.example.hotelfinder.domain.model.Hotel
import com.example.hotelfinder.domain.model.toModel // Importa il mapper
import com.example.hotelfinder.domain.repository.HotelRemoteRepository // Importa l'interfaccia
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // È bene annotarla come Singleton se vuoi una sola istanza
class HotelRetrofitRepository @Inject constructor( // Assicurati che abbia @Inject constructor perche cosi è il dependecyInjection che lo istanzia
    private val hotelService: HotelService
) : HotelRemoteRepository { // <-- Implementa l'interfaccia

    // Implementa il metodo dell'interfaccia
    /* funzione che ritorna una lista di hotel, definisce una variabile response che implementa la funzione di hotelsService
    e ritorna la lista result che viene convertita in una lista di ogetti Hotel
     */
    override suspend fun getHotel(): List<Hotel> {
        val response = hotelService.getHotels()
        return response.results.map { it.toModel() } // Logica corretta di mapping
    }
}