package com.example.hotelfinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hotelfinder.data.local.dao.HotelDao
import com.example.hotelfinder.data.local.entity.LocalHotel

@Database(entities = [LocalHotel::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HotelDatabase: RoomDatabase() {
    // funzione che restituisce un istanza di HotelDao
    abstract fun getHotelDao(): HotelDao



}
