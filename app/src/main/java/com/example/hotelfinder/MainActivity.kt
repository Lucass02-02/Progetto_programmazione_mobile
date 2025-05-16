package com.example.hotelfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hotelfinder.ui.screen.list.ListScreen
import com.example.hotelfinder.ui.screen.map.MapScreen
import com.example.hotelfinder.ui.theme.RestaurantFinderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

//@AndroidEntryPoint serve per poter usare il dependecyInjection
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantFinderTheme {

                //il navigation Controller è colui che sa chi è stato cliccato
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(navController)
                    }
                ) { innerPadding ->
                    //è l'oggetto che ci permette di navigare
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.List //quale è la prima schermata mostrata all avvio(?)
                    ) {
                        // quando è selezionato screen list aprilo
                        composable<Screen.List> {
                            ListScreen(
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        //quando è selezionato screen map aprilo
                        composable<Screen.Map> {
                            MapScreen(
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

// è la barra di navigazione con i due pulsanti list e map nella prima schermata dell'app
@Composable
fun BottomNavigationBar(
    navController: NavHostController //includo il navController
) {
    // remember cosi non viene riaggiornata il continuazione
    val items = remember {
        listOf(
            //Qui è dove viene usata la classe per creare i pulsanti list e map
            BottomNavigationItem(title = "List", icon = Icons.AutoMirrored.Default.List, route = Screen.List),
            BottomNavigationItem(title = "Map", icon = Icons.Default.LocationOn, route = Screen.Map))
    }


    NavigationBar {

        //restituisce la schermata attiva e sa sempre quale è
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        //serve per capire la route in cui ci troviamo
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach{
            NavigationBarItem(
                selected = currentRoute == it.route.javaClass.canonicalName, //ci da tutta la stringa che comprende il package e il nome della classe
                onClick = {
                    //la riga dice se cliccato naviga verso la item route
                    navController.navigate(it.route) {
                        //questi sono degli standard
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(it.icon, contentDescription = it.title) //stessa cosa per l'icona e il contentdesription è per l'accessibilità della nostra applicazione
                },
                label = {
                    Text(text = it.title) // qui c'è il testo, cioè il titolo che abbiamo dato al nostro bottone
                }
            )
        }


    }
}

//Classe per semplificare la creazione della barra di navigazione, rappresenta il pulsante con i suoi attributi
data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: Screen
)

// serve per dire che sono presenti due schermate
sealed class Screen {

    //@Serializable serve per far funzionare il plugin di navigazione
    @Serializable
    data object List: Screen()

    @Serializable
    data object Map: Screen()

}
