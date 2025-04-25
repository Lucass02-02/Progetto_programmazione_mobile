package com.example.hotelfinder.ui.screen.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelfinder.common.Resource
import com.example.hotelfinder.domain.model.Hotel
import com.example.hotelfinder.domain.use_case.GetHotelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//tutto quello che serve per l'interfaccia grafica, quindi il caricamento,l'errore e i dati
data class ListUiState(
    val hotels: List<Hotel> = emptyList(),
    val loadingMsg: String? = null,
    val error: String? = null
)

//sempre per poter usare il dependecyInjection
@HiltViewModel
class ListViewModel @Inject constructor(
    private val getHotelUseCase: GetHotelUseCase
): ViewModel() {

    //stiamo creando uno stato osservabile che varia nel tempo a partire da un certo puntoo cioè ListUiState che abbiamo inizializzato prima
    var uiState by mutableStateOf(ListUiState())
        private set

    //quando viene inizializzato la prima volta farà il download degli hotel
    init {
        downloadHotels()
    }

    //definiamo la funzione per scaricare gli hotel
    private fun downloadHotels() {
        //crea una corutine diversa per evitare conflitti
        viewModelScope.launch {
            //viene ritornato un flow di resource di tipo listHotel
            getHotelUseCase().collect {resource ->
                when(resource) {
                    //quello che succede quando stiamo caricando i dati
                    is Resource.Loading -> {
                        uiState = uiState.copy(
                            loadingMsg = resource.message,
                            error = null
                        )
                    }
                    //quello che succede se i dati sono arrivati con successo
                    is Resource.Success -> {
                        uiState = uiState.copy(
                            hotels = resource.data,
                            loadingMsg = null,
                            error = null
                        )
                    }
                    //quello che succede se c'è stato un errore
                    is Resource.Error -> {
                        uiState = uiState.copy(
                            loadingMsg = null,
                            error = resource.message
                        )
                    }

                }
            }
        }
    }



}