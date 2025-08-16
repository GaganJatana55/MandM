package org.example.mandm

import android.app.Application
import org.example.mandm.di.initKoin
import org.koin.android.ext.koin.androidContext

class ApplicationMain: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@ApplicationMain)
        }
    }
}