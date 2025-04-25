package com.example.hotelfinder.common


sealed class Resource<T> {

    //se tutto è andato bene restituisce i dati
    data class Success<T>(val data: T) : Resource<T>()
    //se c'è stato un errore potrebbe restituire un messaggio di errore
    data class Error<T>(val message: String) : Resource<T>()
    //durante il caricamento dei dati può apparire una scritta che dice Loading
    data class Loading<T>(val message: String?) : Resource<T>()
}