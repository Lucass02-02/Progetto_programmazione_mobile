package com.example.hotelfinder.data.remote.model

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("photo_reference") val photoReference: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int
    // puoi aggiungere altri campi se ti servono
)
