package com.example.hotelfinder.data.local

import com.example.hotelfinder.data.local.dao.HotelDao
import com.example.hotelfinder.data.local.entity.LocalHotel
import com.example.hotelfinder.domain.model.Hotel
import com.example.hotelfinder.domain.repository.HotelLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

fun LocalHotel.toModel(): Hotel = Hotel(
    indirizzo = indirizzo,
    classificazione = classificazione,
    denominazione = denominazione,
    foto = foto,
    posizione = posizione,
    icon = icon,
    iconBackgroundColor = iconBackgroundColor,
    iconMaskBaseUri = iconMaskBaseUri,
    placeId = placeId
)

// Rende Hotel un LocalHotel
fun Hotel.toLocal(): LocalHotel = LocalHotel(
    indirizzo = indirizzo,
    classificazione = classificazione,
    denominazione = denominazione,
    foto = foto,
    posizione = posizione,
    icon = icon,
    iconBackgroundColor = iconBackgroundColor,
    iconMaskBaseUri = iconMaskBaseUri,
    placeId = placeId
)

/* istanza di HotelLocalRepository e ne implementa i metodi astratti */
class HotelRoomRepository @Inject constructor(private val hotelDao: HotelDao) : HotelLocalRepository {

    override suspend fun insert(hotel: Hotel) {
        hotelDao.insert(hotel.toLocal())
    }

    //mappa ogni singolo elemento da Hotel a LocalHotel
    override suspend fun insert(hotels: List<Hotel>) {
        hotelDao.insert(hotels.map(Hotel::toLocal))
    }

    override fun getAll(): Flow<List<Hotel>> {
        return hotelDao.getAll().map { list -> list.map(LocalHotel::toModel) }
    }

    override suspend fun clearAll() {
        hotelDao.deleteAll()
    }

    //per la detail activity
    override fun getHotelByAddress(placeId: String,indirizzo: String, nome: String): Flow<List<Hotel>> {
        return hotelDao.getHotelByAddress(placeId,indirizzo, nome)
            .map { list ->
                    list.map ( LocalHotel::toModel )
            }
    }
}
