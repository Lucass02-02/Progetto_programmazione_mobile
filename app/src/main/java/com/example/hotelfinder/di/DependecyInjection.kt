package com.example.hotelfinder.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hotelfinder.common.BASE_URL
import com.example.hotelfinder.data.local.HotelDatabase
import com.example.hotelfinder.data.local.HotelRoomRepository
// Rimuovi import non usato: import com.example.restaurantfinder.data.remote.ApiResponse
import com.example.hotelfinder.data.remote.HotelService
import com.example.hotelfinder.data.remote.HotelRetrofitRepository
import com.example.hotelfinder.domain.repository.HotelLocalRepository
import com.example.hotelfinder.domain.repository.HotelRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/* file con tutte le configurazioni dei dependency Injection */

@Module
@InstallIn(SingletonComponent::class) /* dice come si comporta il modulo all'interno dell'applicazione. SingletonComponent dice che è disponibile per tutta l'applicazione */
object RetrofitModule {


    /* metodo che restituisce un istanza di retrofit che contiene il builder
    cioè quello che ritorna retrofit, dandogli la BASE_URL e il converitore json
    @Provides perché deve essere chiamato in automatico dal dependecyInjection
    @Singleton perché si deve avere un unica istanza del client retrofit
     */
    @Provides
    @Singleton
    fun retrofitClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    /* metodo per creare un HotelService, gli viene passato retrofit cioè il client creato
    viene ritornato un HotelService tramite il metodo retrofit.create che lo costruisce in base a come abbiamo definito l'interfaccia HotelService
     */
    @Provides
    @Singleton
    fun hotelService(retrofit: Retrofit): HotelService =
        retrofit.create(HotelService::class.java)
}



/* si usa abstract class per implementare le interfacce */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule { // Mantiene 'abstract class'

    /* @Binds si usa per le classi astratte */


    /* è una funzione che lega HotelRemoteRepository e HotelRetrofitRepository perchè HotelRetrofitRepository è
    un istanziazione dell interfaccia HotelRemoteRepository e quindi quando creo un oggetto HotelRemoteRepository
    sa che deve istanziare
     */
    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        repository: HotelRetrofitRepository // Assicurati che HotelRetrofitRepository abbia @Inject constructor
    ): HotelRemoteRepository

    // Questo @Binds è commentato - lascialo così a meno che tu non crei
    // una classe concreta (es. HotelRoomRepository) per il db locale.

    /* è il dependecyinjection che in automatico rende HotelRoomRepository un istanziazione
    dell interfaccia HotelLocalRepository
     */
    @Binds
    @Singleton
    abstract fun bindLocalRepository(repository: HotelRoomRepository): HotelLocalRepository


    // --- Metodi @Provides ---
    // Questi vanno nel companion object

    /*
    companion object {

        @Provides
        @Singleton
        fun provideLocalRepository(database: HotelDatabase): HotelLocalRepository {
            // IMPORTANTE: Fornisci HotelLocalRepository solo UNA volta.
            // Se usi questo @Provides, assicurati che il @Binds bindLocalRepository sopra sia commentato.
            // Questa è l'implementazione MOCK (in memoria):

            return HotelRoomRepository(database.getHotelDao())


            /*return object : HotelLocalRepository {
                private val hotels = mutableListOf<Hotel>()
                override suspend fun insert(hotel: Hotel) { hotels.add(hotel) }
                override suspend fun insert(hotels: List<Hotel>) { this.hotels.addAll(hotels) }
                override fun getAll(): Flow<List<Hotel>> { return flow { emit(hotels) } }
                override suspend fun clearAll() { hotels.clear() }
                override fun getHotelByAddress(indirizzo: String, nome: String): Flow<List<Hotel>> {
                    TODO("Not yet implemented")
                }
            } */
        }

        // Altri metodi @Provides per questo modulo andrebbero qui...
    }*/
}




@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /* con @ApplicationContext sa quale contesto passare
    è una funzione che crea un database grazie ai metodi di Room, gli viene passato il contesto,
    il nome della classe che deve istanziare e il nome del database da creare sulla memoria
     */
    @Provides
    @Singleton
    //ho dovuto fare una migrazione perche ho modificato il database e questo era l unico modo per aggiornarlo
    fun database(@ApplicationContext context: Context): HotelDatabase {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Aggiunta della colonna placeId di tipo TEXT
                database.execSQL("ALTER TABLE hotels ADD COLUMN placeId TEXT")
            }
        }
        return Room.databaseBuilder(
            context,
            HotelDatabase::class.java,
            "hotel_db"
        )
            .addMigrations(MIGRATION_1_2) // ✅ ORA è visibile
            .build()

    }



    // la funzione ha in ingresso un ogetto HotelDatabase e usa la funzione getHotelDao per ottenere un HotelDao
    @Provides
    @Singleton
    fun hotelDao(database: HotelDatabase) = database.getHotelDao() // Giusto: fornisce HotelDao
}