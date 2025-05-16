package com.example.hotelfinder.ui.screen.map

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelfinder.common.LocationHelper
import com.example.hotelfinder.common.Resource
import com.example.hotelfinder.domain.model.Hotel
import com.example.hotelfinder.domain.use_case.GetHotelUseCase
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

data class  MapUIState (
    val  hotel: List<Hotel> = emptyList(),
    val  loadingMsg: String? =null,
    val  error: String? = null,
    val  markerState: MarkerState? = null,
    //inizializziamo la posizione della camera in un certo punto della mappa
    val  cameraPositionState: CameraPositionState = CameraPositionState(
        position = CameraPosition(
           LatLng(0.0,0.0),
            10f,
            0f,
            0f
        )
    ),
    val  filteredHotels: List<Hotel> = emptyList(),
)

//qui definiamo i data object dello start location e stop location che abbiamo creato nel locacionhelper
sealed class MapEvent {
    data object StartLocation: MapEvent()
    data object StopLocation: MapEvent()
}


//usa il dependencyInjection quindi inseriamo gli @
@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val  getHotelUseCase: GetHotelUseCase,
) : ViewModel (){

    //definiamo lo uiState
    var  uiState by mutableStateOf(MapUIState())
        private set

    //qui inizializzo il locationhelper
    private val locationHelper = LocationHelper(context = context) { location ->
        //recupero la mia posizione
        val markerState = MarkerState(
            position = LatLng(location.latitude, location.longitude)
        )

        //ogni volta che cambia la posizione aggiorna e raggiungila
        val cameraPosition = CameraPosition(
            LatLng(location.latitude, location.longitude),
            13f,
            0f,
            0f
        )

        //qui filtro gli hotel cioe li faccio mostrare solo se sono ad una certa distanza da me
       val filteredHotels = uiState.hotel.filter {
            val hotelLocation = android.location.Location("hotel")
                .apply {
                    latitude = it.posizione.location.lat
                    longitude = it.posizione.location.lng
                }
            location.distanceTo(hotelLocation) <= 10000
        }

        uiState = uiState.copy(
            markerState = markerState,
            filteredHotels = filteredHotels,
            cameraPositionState = CameraPositionState(
                position = cameraPosition
            )
        )
    }

    //scrichiamo gli hotel
    init {
        getHotel()
    }

    //creiamo una funzione dove passiamo un mapevent, se è di tipo start allora avvia la localizzazione , se è stop ferma la localizzazione
    fun onEvent(event: MapEvent) {
        when(event) {
            is MapEvent.StartLocation -> {
                locationHelper.start()
            }
            is MapEvent.StopLocation -> {
                locationHelper.stop()
            }
        }
    }

    //come per la lista
    private fun getHotel(){

        viewModelScope.launch{
            getHotelUseCase().collect { resource ->
                uiState = when(resource) {
                    is Resource.Loading ->{
                        uiState.copy(
                            loadingMsg = resource.message,
                            error = null,
                        )
                    }
                    is Resource.Success -> {
                        uiState.copy(
                            hotel = resource.data,
                            loadingMsg = null,
                            error = null,
                        )
                    }
                    is Resource.Error -> {
                        uiState.copy(
                            loadingMsg = null,
                            error =  resource.message,
                        )
                    }
                }
            }
        }
    }
}