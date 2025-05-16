package com.example.hotelfinder.domain.model

import com.example.hotelfinder.data.remote.model.Geometry
import com.example.hotelfinder.data.remote.model.Photo
import com.example.hotelfinder.data.remote.model.RemoteHotel


/*inseriamo una serie di attributi che potrebbero servire nell'interfaccia grafica*/

data class Hotel(
    val indirizzo: String,
    val classificazione: Double?,  // Cambiato da String a Double?
    val denominazione: String,
    val foto: List<Photo>?,  // Può essere nullo
    val posizione: Geometry,
    val icon: String,
    val iconBackgroundColor: String,
    val iconMaskBaseUri: String,
    val placeId: String


)

fun RemoteHotel.toModel(): Hotel {
    return Hotel(
        indirizzo = this.formattedAddress,
        classificazione = this.rating,
        denominazione = this.name,
        foto = this.photos,
        posizione = this.geometry,
        icon = this.icon,
        iconBackgroundColor = this.iconBackgroundColor,
        iconMaskBaseUri = this.iconMaskBaseUri,
        placeId = this.placeId,
    )
}