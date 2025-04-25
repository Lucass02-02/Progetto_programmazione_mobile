package com.example.hotelfinder.ui.screen.detail

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.hotelfinder.domain.model.Hotel
import coil.compose.AsyncImage
import com.google.maps.android.ktx.BuildConfig


@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
    indirizzo: String?,
    nome: String?,
    valutazione: String?,
    placeId: String?
) {
    //definisco la activity
    val activity = LocalContext.current as Activity

    //recupero lo uistate
    val uiState = viewModel.uiState

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.onEvent(
            DetailEvent.OnHotelSelected(
                indirizzo = indirizzo,
                nome = nome,
                valutazione = valutazione,
                placeId = placeId
            )
        )
    }

    //contenuto della schermata
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail") },
                navigationIcon = {
                    IconButton(onClick = {activity.finish() }) {
                        Icon(contentDescription = "Back", imageVector = Icons.AutoMirrored.Default.ArrowBack)
                    }
                }
            )
        }
    ){padding ->

        //se lo uistate è vuoto mostra la scritta nessun hotel trovato
        if (uiState.hotel.isEmpty())    {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Nessun hotel trovato")
            }
            return@Scaffold
        }

         LazyColumn (
             modifier = Modifier
                 .fillMaxSize()
                 .padding(padding),
             contentPadding = PaddingValues(16.dp)
         ) {
                items(uiState.hotel.size) {
                    HotelItem(
                        modifier = Modifier.fillMaxWidth(),
                        hotel = uiState.hotel[it]
                    )
                }
         }
    }
}

//come è fatto il singolo hotel
@Composable
fun HotelItem(
    modifier: Modifier = Modifier,
    hotel: Hotel,
) {
    Card(
        modifier = modifier
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        ) {
            // Mostra immagine se presente
            hotel.foto?.firstOrNull()?.let { photo ->
                val apiKey = com.example.hotelfinder.BuildConfig.PLACE_API_KEY
                val imageUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${photo.photoReference}&key=$apiKey"

                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Foto hotel",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Text(text = "Indirizzo: ${hotel.indirizzo}")
            Text(text = "Nome: ${hotel.denominazione}")
            Text(text = "Valutazione: ${hotel.classificazione}")
            Text(text = "PlaceID: ${hotel.placeId}")
            Text(text = "longitudine: ${hotel.posizione.location.lng}")
            Text(text = "latitudine: ${hotel.posizione.location.lat}")

        }
    }
}