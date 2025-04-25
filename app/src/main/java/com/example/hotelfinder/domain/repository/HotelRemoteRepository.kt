package com.example.hotelfinder.domain.repository

import com.example.hotelfinder.domain.model.Hotel

/*interfaccia con metodo che ritorna una lista di Hotel*/

interface HotelRemoteRepository {
    suspend fun getHotel(): List<Hotel>
}