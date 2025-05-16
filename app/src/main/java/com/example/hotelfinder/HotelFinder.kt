package com.example.hotelfinder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// è necessaria per il dependecyInjection
//classe che gestisce il ciclo di vita dell'applicazione, perche ad esempio non uso oncreate() nel main?
//perche sono due oncreate() diversi, quello del main(dell'activity) è quello della pagina dell'activity
//invece l oncreate() di questa classe è quello dell'applicazione, la differenza è che gestisce la vita di tutta l'app
//non muore quando chiudo l'activity ma quando chiudo l'app
//in pratica dico che viglio creare un app che usa il pacchetto hilt
@HiltAndroidApp
class HotelFinder: Application() {
}