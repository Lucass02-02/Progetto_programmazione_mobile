package com.example.hotelfinder.ui.screen.detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelfinder.domain.model.Hotel
import com.example.hotelfinder.domain.repository.HotelLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

//lo uistate parte sempre da una lista vuota
data class DetailUiState(
    val hotel: List<Hotel> = emptyList()
)

sealed class DetailEvent{
    data class OnHotelSelected(val placeId: String?,val indirizzo: String?, val nome: String?, val valutazione: String?): DetailEvent()
}


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val localRepository: HotelLocalRepository
): ViewModel() {

    var uiState by mutableStateOf(DetailUiState())
        private set

    fun onEvent(event: DetailEvent) {
        when(event) {
            is DetailEvent.OnHotelSelected -> {

                viewModelScope.launch {


                    localRepository.getHotelByAddress(event.placeId ?:"",event.indirizzo ?: "", event.nome ?: "").collect {
                        uiState = uiState.copy(
                            hotel = it
                        )
                    }
                }
            }
        }
    }

}
