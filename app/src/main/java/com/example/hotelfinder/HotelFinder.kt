package com.example.hotelfinder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// è necessaria per il dependecyInjection
@HiltAndroidApp
class HotelFinder: Application() {
}