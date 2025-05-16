package com.example.hotelfinder.ui.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlin.system.exitProcess

//sealed class in modo da poterla usare in modo più generale possibile
sealed class Permission(
    val title: String,
    val message: String,
    val permissions: List<String>
)

//classe per i permessi della posizione
class LocationPermission: Permission(
    title = "Location Permission",
    message = "Your app needs access to your location to show you the closest hotel",
    permissions = listOf( // questi sono i permessi che andiamo a dare
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
)

//classe con il tipo di evento e l azione da fare quando avviene quell evento
class LifecycleEvent(
    val event: Lifecycle.Event,
    val action: () -> Unit
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable //serve per gestire i permessi all'interno di questo file
fun PermissionChecker(
    permission: Permission,
    events: List<LifecycleEvent> = emptyList(), //passiamo la lista di lifecycle event
    content: @Composable () -> Unit = {}
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = permission.permissions //gli diamo la lista dei nostri permessi da verificare
    )

    if (permissionState.allPermissionsGranted) { // se ho dato i permessi
        content()

        //dopo che la mappa viene visualizzara correttamente, per ogni evento realizzo un event effect, gli passo l evento e in base all'evento agisce l action
        events.forEach {
            LifecycleEventEffect(it.event) {
                it.action()
            }
        }
    } else {
        if(permissionState.shouldShowRationale){// se non ho mai chiesto perche servono i permessi dagli il dialog
            PermissionDialog( // qui è come si comporta quando l utente fa una di queste azioni
                permission = permission,
                onDismiss = { exitProcess(0) },
                onRequest = {permissionState.launchMultiplePermissionRequest()}
            )
        } else {
            SideEffect { // se non serve il dialog chiedo solo i permessi, si usa sideeffect cosi sa che deve andare in esecuzione non dentro un composable
                permissionState.launchMultiplePermissionRequest()
            }
        }
    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun PermissionDialog( // ci va a dire il motivo per cui servono i permessi
    permission: Permission = LocationPermission(),
    onDismiss: () -> Unit = {}, //per il pulsante per rifiutare
    onRequest: () -> Unit = {}  // per il pulsante accettare
) {
    BasicAlertDialog(
        onDismissRequest = {}
    ) {
        Column(
            modifier = Modifier // per la grafica
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( //il titolo
                text = permission.title,
                style = MaterialTheme.typography.titleLarge
            )

            Text( //il messaggio
                modifier = Modifier.padding(top = 16.dp),
                text = permission.message,
                style = MaterialTheme.typography.bodyMedium
            )

            Row ( // bottoni per accettare o rifiutare
                modifier = Modifier.padding(16.dp),
            ) {
                OutlinedButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = onRequest) {
                    Text(text = "Request")
                }
            }
        }
    }
}