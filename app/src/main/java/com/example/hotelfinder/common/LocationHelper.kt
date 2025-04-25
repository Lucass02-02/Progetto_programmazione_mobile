package com.example.hotelfinder.common

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat

//gli diamo il contesto e una funzione che prende in ingresso la posizione e restituisce uno unit
class LocationHelper (private val context: Context, private val onLocationChanged: (Location) -> Unit) {

    //abbiamo bisongo di un location managr che viene restituito dal sistema operativo, lo diamo come locationmanager perche ritorna qualsiasi manager
    private val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?


    private val listener = LocationListener { location->
        onLocationChanged(location)
    }

    fun start() {

        //controlla se il gps e i servizi netework sono abilitati
        val isGPSEnabled = manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        val isNetworkEnabled = manager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

        //controlla se sono stati concessi i permessi per accedere alla posizione precisa e approssimativa
        val isFineGranted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val isCoarseGranted = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED


        //se il gps è attivo e il permesso è concesso usa il gps per ottenere informazioni, altrimenti se il provider è attivo e il permesso è concesso usa la rete
        if (isGPSEnabled && isFineGranted) {
            manager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, listener)
        } else if (isNetworkEnabled && isCoarseGranted) {
            manager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, listener)
        }
        //preferiamo il gps perche è piu preciso, il provider solo se il gps non funziona

    }

    fun stop() {
        manager?.removeUpdates(listener)
    }
}