package com.example.hotelfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hotelfinder.ui.screen.detail.DetailScreen
import com.example.hotelfinder.ui.theme.RestaurantFinderTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantFinderTheme {

                val address = intent.getStringExtra("indirizzo")
                val nome = intent.getStringExtra("nome")
                val valutazione = intent.getStringExtra("valutazione")
                val placeId = intent.getStringExtra("place_id")


                DetailScreen(
                    indirizzo = address,
                    nome = nome,
                    valutazione = valutazione,
                    placeId = placeId

                )
            }
        }
    }
}