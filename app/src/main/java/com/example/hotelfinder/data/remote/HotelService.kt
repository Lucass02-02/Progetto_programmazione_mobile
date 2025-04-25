package com.example.hotelfinder.data.remote

import com.example.hotelfinder.common.API_DATA
import retrofit2.http.GET



/* questo metodo getHotels restutuisce un ogetto di tipo api response
 che contiene la lista results che contiene i dati del json
 */

interface HotelService {
    @GET(API_DATA) // Assicurati che API_DATA sia definito
    suspend fun getHotels(): ApiResponse // Ora restituisce un oggetto ApiResponse
}