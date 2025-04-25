package com.example.hotelfinder.ui.screen.map

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.example.hotelfinder.DetailActivity
import com.example.hotelfinder.ui.tools.LifecycleEvent
import com.example.hotelfinder.ui.tools.LocationPermission
import com.example.hotelfinder.ui.tools.PermissionChecker
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel() //recuperiamo il viewmodel con il dependecyInjection
) {
   val uiState = viewModel.uiState //prendiamo lo uiState del viewmodel
    val context = LocalContext.current

    //controllo i permessi 
    PermissionChecker(
        permission = LocationPermission(),
        //i miei eventi sono una lista di due lifecycle event, su on resume faccio partire la localizzazione, su on pause fermo la localizzazione
        events = listOf(
            LifecycleEvent(event = Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(MapEvent.StartLocation)
            },
            LifecycleEvent(event = Lifecycle.Event.ON_PAUSE) {
                viewModel.onEvent(MapEvent.StopLocation)
            }
        )
    ) {
        //aggiungiamo la google map
        GoogleMap(
            modifier = modifier,
            cameraPositionState = uiState.cameraPositionState
        ) {
            //se esiste lo stato sulla mia posizione crea un marker blu
            uiState.markerState?.let {
                Marker(
                    state = it,
                    title = "Current Location",
                    snippet = "Your current location",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                )
            }

            uiState.filteredHotels.forEach { hotel -> //per ogni hotel crea un marker
                Marker(
                    state = rememberMarkerState( //remember se la posizione è statica e gli diamo le posizioni prese dal json
                        position = LatLng(hotel.posizione.location.lat, hotel.posizione.location.lng)
                    ),
                    title = hotel.denominazione,
                    snippet = "${hotel.indirizzo}",
                    //quando clicco passa alla detailActivity
                    onInfoWindowClick = {
                        context.startActivity(Intent(context, DetailActivity::class.java)
                            .apply {
                                putExtra("indirizzo", hotel.indirizzo)
                                putExtra("nome", hotel.denominazione)
                                putExtra("valutazione", hotel.classificazione)
                                putExtra("Posizione (lat)", hotel.posizione.location.lat)
                                putExtra("Posizione (lng)", hotel.posizione.location.lng)
                                putExtra("place_id", hotel.placeId)
                            })
                    }

                )


            }

        }
    }
}

