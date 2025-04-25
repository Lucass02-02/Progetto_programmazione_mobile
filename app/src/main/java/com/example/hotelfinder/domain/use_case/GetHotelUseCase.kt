package com.example.hotelfinder.domain.use_case

import com.example.hotelfinder.common.Resource
import com.example.hotelfinder.domain.model.Hotel
import com.example.hotelfinder.domain.repository.HotelLocalRepository
import com.example.hotelfinder.domain.repository.HotelRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject


//usiamo uno use case per prendere i dati dal repository remote e inserirli in quello locale e poi farli usare dal view model
//inoltre inseriamo dei messaggi per far capire all utente lo stato dell operazione (messaggi di loading/errore)
class GetHotelUseCase @Inject constructor(
    private val remoteRepo: HotelRemoteRepository,
    private val localRepo: HotelLocalRepository
) {

    //metodo che ritorna un flow di una lista di hotel, c'è resource perche deve poter contenere errori e caricamenti
    operator fun invoke(): Flow<Resource<List<Hotel>>> {
        return flow {
            emit(Resource.Loading("Loading...")) //all inizio parte un loading

            /*val localData = localRepo.getAll().firstOrNull() // Prendiamo la lista senza ascoltare i cambiamenti continui

            if (localData.isNullOrEmpty()) {
                try {
                    val data = remoteRepo.getHotel()
                    emit(Resource.Success(data)) // Emettiamo subito il successo, prima di salvarli

                    localRepo.insert(data) // Salviamo senza attivare un altro flow
                } catch (e: HttpException) {
                    emit(Resource.Error("Error ${e.message()}"))
                } catch (e: Exception) {
                    emit(Resource.Error("Generic Error ${e.message}"))
                }
            } else {
                emit(Resource.Success(localData))
            }*/
            /*verifico se nel database locale c'è qualcosa, con catch verifico se c'è un errore nel flow
            con collect recupero i dati, se la lista è vuota prendi i dati dal remoteRepo e li inserisco nel local
            uso try e catch perché retrofit potrebbe andare in errore, una volta scaricati tutti i dati do un success
             */

           localRepo.getAll()
                .catch { emit(Resource.Error("Error ${it.message}")) }
                .collect { list -> if(list.isEmpty()) {
                    //remote request
                    try {
                      val data =  remoteRepo.getHotel()
                      localRepo.insert(data)
                      emit(Resource.Success(data))

                    } catch (e: HttpException) {
                        emit(Resource.Error("Error ${e.message()}"))
                    }

                } else {
                    emit(Resource.Success(list))
                }}
        }
    }
}