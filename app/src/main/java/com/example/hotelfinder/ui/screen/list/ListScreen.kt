package com.example.hotelfinder.ui.screen.list

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hotelfinder.DetailActivity
import com.example.hotelfinder.domain.model.Hotel

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel() //oltre al modifier gli passo il viewmodel ma con hitl perche lo fa il dependecyInjection
){
    val uiState = viewModel.uiState //definisco lo stato

    val context = LocalContext.current

    //che fa quando sto caricando la schermata o magari sto scaricando dati dal database
    if (uiState.loadingMsg != null) {
        //creiamo un box che mette il messaggio al centro della schermata
        Box(modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(text = uiState.loadingMsg)
        }
        return
    }

    //che fa quando ho un errore
    if (uiState.error != null) {
        //stessa cosa fatta per il caricamento
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(text = uiState.error)
        }
        return
    }

    //quando invece ho successo creo una colonna che contiene i dati
    Column(modifier = modifier) {
        //è il titolo della schermata
        Text(
            modifier=Modifier.padding(16.dp),
            text = "Hotels",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        //La colonna che contiene la lista di hotel
        LazyColumn(
            modifier = Modifier.fillMaxWidth() //occupa tutto lo spazio possibile
        ) {
            //gli viene data la lunghezza di hotels e ne accede ad ogni singolo elemento
            items(uiState.hotels.size){ index ->
                val hotel = uiState.hotels[index]
                HotelItem( //qui passiamo il singolo hotel con tutti i suoi attributi
                    modifier = Modifier.fillMaxWidth(),
                    hotel = hotel,
                    onItemClick = {
                        //usiamo startactivity oerche ci stiamo muovendo tra activity, e passiamo anche il context e la destinazione
                       context.startActivity(Intent(context, DetailActivity::class.java)
                           .also {
                                it.putExtra("indirizzo", hotel.indirizzo)
                                it.putExtra("nome", hotel.denominazione)
                                it.putExtra("valutazione", hotel.classificazione)
                                it.putExtra("Posizione (lat)", hotel.posizione.location.lat)
                                it.putExtra("Posizione (lng)", hotel.posizione.location.lng)
                                it.putExtra( "place_id", hotel.placeId)
                           })
                    }
                )
            }
        }
    }

}




//definiamo come è fatto il singolo hotel
@Composable
fun HotelItem(
    modifier: Modifier,
    hotel: Hotel,
    onItemClick: (Hotel) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .clickable { //la funzione che ci permette di passare da una parte all'altra
                onItemClick(hotel)
            }
    ) {
        Text( //il testo più grande
            text = hotel.denominazione,
            style = MaterialTheme.typography.bodyLarge
        )
        Text( //il testo più piccolo
            text = "${hotel.indirizzo}, ${hotel.classificazione}, ${hotel.denominazione}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}



