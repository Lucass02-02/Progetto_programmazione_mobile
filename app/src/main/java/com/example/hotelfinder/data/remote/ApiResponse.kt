package com.example.hotelfinder.data.remote


import com.example.hotelfinder.data.remote.model.RemoteHotel
import com.google.gson.annotations.SerializedName


/* essendo che il json restituisce un ogetto che contiene un array
 "results" che contiene i dati questa classe converte quella lista in un ogetto
 */

data class ApiResponse(
    @SerializedName("results") val results: List<RemoteHotel>
)
